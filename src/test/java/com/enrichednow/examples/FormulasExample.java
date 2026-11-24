package com.enrichednow.examples;

import com.enrichednow.EnrichedNowClient;
import com.enrichednow.FormulaResult;
import com.enrichednow.FormulaRowListParams;
import com.enrichednow.FormulaRunOptions;
import com.enrichednow.Notify;
import com.enrichednow.RowVerdict;
import com.enrichednow.SavedFormulaChange;
import com.enrichednow.SavedFormulaCreate;
import com.enrichednow.Schedule;
import com.enrichednow.SuppressionEntry;
import com.enrichednow.errors.AttestationRequiredException;
import com.enrichednow.errors.FormulaRunFailedException;
import com.enrichednow.errors.LimitReachedException;
import com.enrichednow.errors.PlanLimitReachedException;
import com.enrichednow.errors.RequirementMissingException;
import com.enrichednow.errors.RunTimeoutException;
import com.enrichednow.formulas.JobOrderHunterParams;
import com.enrichednow.models.FormulaEstimate;
import com.enrichednow.models.FormulaRow;
import com.enrichednow.models.RecordFormat;
import com.enrichednow.models.SavedFormula;
import com.enrichednow.models.SuppressionUpload;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

/**
 * Formulas, start to end: estimate, preview, run and read the rows, save with a schedule and poll for new rows, keep
 * people out with the suppression list, and say when a row is wrong. Compiled with the tests; it is not run by them.
 *
 * <p>To run it against the API: set {@code ENRICHEDNOW_API_KEY} and call {@link #main(String[])}.
 */
public final class FormulasExample {
    private FormulasExample() {
    }

    /**
     * @param args not used
     * @throws IOException when the CSV file cannot be written
     */
    public static void main(String[] args) throws IOException {
        EnrichedNowClient client = EnrichedNowClient.fromEnv();

        // The parameters, typed: one class each formula, with a builder that refuses to build without the required ones.
        JobOrderHunterParams params = JobOrderHunterParams.builder()
                .jobTitles("Java Developer", "DevOps Engineer")
                .locations("London")
                .postedWithinDays(2)
                .companySize(11, 5000)
                .build();

        // What would it cost? Spends nothing.
        FormulaEstimate cost = client.formulas().estimate(params, FormulaRunOptions.builder().unitCap(2000).build());
        if (!cost.valid()) {
            cost.errors().forEach(problem -> System.err.println(problem.path() + ": " + problem.message()));
            return;
        }
        System.out.println("about " + cost.estimate().expectedUnits() + " units, at most " + cost.estimate().maxUnits());

        try {
            // A preview first: at most 10 rows and 50 units.
            FormulaResult preview = client.runFormula(params, FormulaRunOptions.preview());
            for (FormulaRow row : preview) {
                System.out.println(row.get("company_name") + " - " + row.get("job_title"));
            }

            // The full run, capped; waits for it to end and pages through its rows as they are read.
            FormulaResult result = client.runFormula(params, FormulaRunOptions.builder()
                    .unitCap(2000)
                    .timeout(Duration.ofMinutes(15))
                    .build());
            System.out.println(result.run().status() + ": " + result.run().rowsTotal() + " rows, " + result.run().unitsSpent() + " units");
            for (FormulaRow row : result.rows(FormulaRowListParams.builder().fields("company_name", "contact_email").minScore(10).build())) {
                System.out.println(row.get("company_name") + " <" + row.get("contact_email") + ">");
            }
            try (OutputStream out = Files.newOutputStream(Path.of("job-orders.csv"))) {
                result.exportAll(RecordFormat.CSV, out);                    // one file, the header row once
            }

            // A row that names the agency instead of the employer: say so, and later runs match the right company.
            result.rows().stream().findFirst().ifPresent(row ->
                    client.formulaRows().feedback(row.id(), RowVerdict.WRONG_COMPANY, "The agency, not the employer.", "acme.example"));
        } catch (AttestationRequiredException e) {
            System.err.println("An owner or an admin must accept the " + e.kind() + " terms in the console first.");
        } catch (RequirementMissingException e) {
            System.err.println("The account lacks " + e.missing());
        } catch (FormulaRunFailedException e) {
            System.err.println("The run ended " + e.status() + " (" + e.reason() + ")");
        } catch (RunTimeoutException e) {
            System.err.println("Still going: " + e.runId());
        }

        // Saved with a schedule: each run delivers only rows it has not delivered before.
        try {
            SavedFormula saved = client.savedFormulas().create(SavedFormulaCreate.builder(params)
                    .schedule(Schedule.weekdays("07:30", "Europe/London"))
                    .monthlyUnitCap(20000)
                    .notify(Notify.DIGEST)
                    .build());

            Instant lastPoll = Instant.now().minus(Duration.ofDays(1));
            for (FormulaRow row : client.savedFormulas().rows(saved.id(), FormulaRowListParams.builder().since(lastPoll).build())) {
                System.out.println("new: " + row.get("company_name"));
            }

            client.savedFormulas().update(saved.id(), SavedFormulaChange.builder().removeSchedule().build());
        } catch (PlanLimitReachedException e) {
            System.err.println("The plan allows " + e.allowed() + " for " + e.limit());
        } catch (LimitReachedException e) {
            System.err.println("The " + e.scope() + " cap is reached");
        }

        // The suppression list: what runs leave out.
        client.suppressions().add(SuppressionEntry.domain("competitor.example"), SuppressionEntry.email("someone@client.example"));
        SuppressionUpload upload = client.suppressions().upload("kind,value\ndomain,client-one.example\nemail,jo@client-two.example\n");
        System.out.println(upload.added() + " added, " + upload.already() + " already there, " + upload.invalid().size() + " unreadable");
    }
}
