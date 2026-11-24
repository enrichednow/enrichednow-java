# Changelog

What changed in each version of the library. Versions before 0.4.0 are in the API's changelog,
<https://docs.enrichednow.com/changelog/>.

## 0.4.0 (not released yet)

Formulas: ready-made workflows that turn a few parameters into a finished list of rows.

- `client.formulas()`: `list` (a query in your own words, best matches first; each formula's `basics()`, the questions
  it asks), `get`, `estimate` (free; parameters that cannot run come back with `valid()` false and
  what to change) and `run` (`FormulaRunOptions`: preview, unit cap, row cap, wait, and an `Idempotency-Key` on every
  start, as on a scraper run).
- `client.formulaRuns()`: `list`, `get`, `waitFor` (each look held by the API for up to a minute), `rows` across
  pages (`FormulaRowListParams`: fields, least score, one row per company), `exportPage` and `exportAll` as CSV or
  NDJSON, `cancel`.
- `client.runFormula(...)`: starts a run, waits for it and returns a `FormulaResult` with the run and its rows;
  `FormulaRunFailedException` when the run fails.
- `client.savedFormulas()`: `create` (`SavedFormulaCreate`, with a `Schedule`), `list`, `get`, `update`
  (`SavedFormulaChange`: `removeSchedule()`, `removeMonthlyUnitCap()`, `autoName()`), `archive`, `duplicate`, `run`,
  `pause`, `resume`, `resetSeen`, `rows` since a time, exports, `snapshots`, `limits`.
- `client.suppressions()` (`list`, `add`, `upload` a CSV file, `delete`), `client.attestations().list()` and
  `client.formulaRows().feedback(...)`; the same on `client.async()`.
- `com.enrichednow.formulas`: each formula's parameters as a class with a builder (`JobOrderHunterParams`, …); the
  calls that take one read the formula's id from it.
- New exceptions: `PlanLimitReachedException` (403, a `PermissionDeniedException`), `ConflictException` (409) and
  under it `AttestationRequiredException`, `FormulaUnavailableException`, `RequirementMissingException`,
  `SavedFormulaArchivedException`. `LimitReachedException.savedFormulaId()` for a saved formula's monthly cap;
  `ValidationException.minUnitCap()` for `unit_cap_too_low`.
