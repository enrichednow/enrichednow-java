package com.enrichednow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.enrichednow.models.Company;
import com.enrichednow.models.Posting;
import com.enrichednow.models.PostingDetail;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class DataTest {
    private final FakeApi api = new FakeApi();

    @AfterEach
    void stop() {
        api.close();
    }

    @Test
    void postingsAreSearchedAndReadWhole() {
        api.ok("{\"postings\":[" + FakeApi.posting("p_1") + "],\"next_cursor\":null}");
        List<Posting> postings = api.client().data().postings().list().toList();
        assertEquals("GET /v1/data/postings", api.only().toString());
        Posting posting = postings.get(0);
        assertEquals("p_1", posting.id());
        assertEquals("Senior Software Engineer (m/f/d)", posting.title());
        assertEquals("Senior Software Engineer", posting.titleClean());
        assertEquals("acme.com", posting.company().domain());
        assertEquals("Acme", posting.company().name());
        assertEquals("Berlin", posting.location().city());
        assertEquals("DE", posting.location().countryCode());
        assertNull(posting.location().region());
        assertNull(posting.location().remote());
        assertEquals("full_time", posting.employmentType());
        assertEquals("senior", posting.seniority());
        assertEquals("engineering", posting.function());
        assertEquals(80000.0, posting.salary().min());
        assertEquals(100000.5, posting.salary().max());
        assertEquals("EUR", posting.salary().currency());
        assertNull(posting.applyUrl());
        assertEquals("greenhouse", posting.source().name());
        assertEquals("feed", posting.source().provenance());
        assertTrue(posting.source().trusted());
        assertTrue(posting.isOpen());
        assertEquals(LocalDate.of(2026, 11, 3), posting.postedAt());
        assertNull(posting.validThrough());
        assertEquals(Instant.parse("2026-11-04T08:00:00Z"), posting.firstSeenAt());
        assertNull(posting.closedAt());
    }

    @Test
    void everyPostingFilterIsSentUnderItsOwnName() {
        api.ok("{\"postings\":[],\"next_cursor\":null}");
        PostingListParams params = PostingListParams.builder()
                .companyDomain("acme.com").title("software engineer").location("Berlin").country("DE").remote(true)
                .employmentType("full_time").seniority("senior").function("engineering").status("open").source("greenhouse")
                .provenance("feed,page").unmatched(true)
                .postedFrom(LocalDate.of(2026, 11, 1)).postedTo(LocalDate.of(2026, 11, 17))
                .firstSeenFrom(Instant.parse("2026-11-01T00:00:00Z")).firstSeenTo(Instant.parse("2026-11-17T00:00:00Z"))
                .lastSeenFrom(Instant.parse("2026-11-16T00:00:00Z")).lastSeenTo(Instant.parse("2026-11-18T00:00:00Z"))
                .sort("last_seen_at").limit(200).cursor("c0").build();
        api.client().data().postings().listPage(params);
        assertEquals("company_domain=acme.com&title=software%20engineer&location=Berlin&country=DE&remote=true"
                + "&employment_type=full_time&seniority=senior&function=engineering&status=open&source=greenhouse"
                + "&provenance=feed%2Cpage&unmatched=true&posted_from=2026-11-01&posted_to=2026-11-17"
                + "&first_seen_from=2026-11-01T00%3A00%3A00Z&first_seen_to=2026-11-17T00%3A00%3A00Z"
                + "&last_seen_from=2026-11-16T00%3A00%3A00Z&last_seen_to=2026-11-18T00%3A00%3A00Z"
                + "&sort=last_seen_at&limit=200&cursor=c0", api.only().query);
    }

    @Test
    void postingsArePagedWithTheSameFilters() {
        api.ok("{\"postings\":[" + FakeApi.posting("p_1") + "," + FakeApi.posting("p_2") + "],\"next_cursor\":\"n1\"}");
        api.ok("{\"postings\":[" + FakeApi.posting("p_3") + "],\"next_cursor\":null}");
        PostingListParams params = PostingListParams.builder().status("open").limit(2).build();
        List<String> ids = api.client().data().postings().list(params).stream().map(Posting::id).collect(Collectors.toList());
        assertEquals(List.of("p_1", "p_2", "p_3"), ids);
        assertEquals("status=open&limit=2", api.received().get(0).query);
        assertEquals("status=open&limit=2&cursor=n1", api.received().get(1).query);
    }

    @Test
    void onePostingComesWithItsDescriptionAndHistory() {
        String body = FakeApi.posting("p_1");
        body = body.substring(0, body.length() - 1) + ",\"description\":\"We are looking for an engineer.\","
                + "\"classification\":{\"seniority_rule\":\"title:senior\",\"function_rule\":null},"
                + "\"history\":{\"events\":[{\"kind\":\"first_seen\",\"at\":\"2026-11-04T08:00:00Z\"},{\"kind\":\"closed\",\"at\":\"2026-11-10T08:00:00Z\"}],"
                + "\"changes\":[{\"field\":\"title\",\"old\":\"Engineer\",\"new\":\"Senior Engineer\",\"at\":\"2026-11-05T08:00:00Z\"}],"
                + "\"reopened_count\":1,\"sightings\":{\"count\":9,\"recent\":[\"2026-11-17T08:00:00Z\",\"2026-11-16T08:00:00Z\"]}}}";
        api.ok(body);
        PostingDetail posting = api.client().data().postings().get("p_1");
        assertEquals("GET /v1/data/postings/p_1", api.only().toString());
        assertEquals("p_1", posting.id());
        assertEquals("We are looking for an engineer.", posting.description());
        assertEquals("title:senior", posting.classification().seniorityRule());
        assertNull(posting.classification().functionRule());
        assertEquals("closed", posting.history().events().get(1).kind());
        assertEquals(Instant.parse("2026-11-10T08:00:00Z"), posting.history().events().get(1).at());
        assertEquals("title", posting.history().changes().get(0).field());
        assertEquals("Engineer", posting.history().changes().get(0).oldValue());
        assertEquals("Senior Engineer", posting.history().changes().get(0).newValue());
        assertEquals(1, posting.history().reopenedCount());
        assertEquals(9, posting.history().sightings().count());
        assertEquals(Instant.parse("2026-11-16T08:00:00Z"), posting.history().sightings().recent().get(1));
    }

    @Test
    void companiesAreListedAndPaged() {
        api.ok("{\"companies\":[" + FakeApi.company("acme.com") + "],\"next_cursor\":\"n1\"}");
        api.ok("{\"companies\":[" + FakeApi.company("acme.org") + "],\"next_cursor\":null}");
        CompanyListParams params = CompanyListParams.builder().q("acme").ats("greenhouse").limit(1).build();
        List<Company> companies = api.client().data().companies().list(params).toList();
        assertEquals("GET /v1/data/companies?q=acme&ats=greenhouse&limit=1", api.received().get(0).toString());
        assertEquals("GET /v1/data/companies?q=acme&ats=greenhouse&limit=1&cursor=n1", api.received().get(1).toString());
        assertEquals(List.of("acme.com", "acme.org"), companies.stream().map(Company::domain).collect(Collectors.toList()));

        api.ok("{\"companies\":[],\"next_cursor\":null}");
        assertTrue(api.client().data().companies().list().toList().isEmpty());
        assertEquals("GET /v1/data/companies", api.last().toString());
    }

    @Test
    void oneCompanyIsReadByDomain() {
        api.ok(FakeApi.company("acme.com"));
        Company company = api.client().data().companies().get("www.acme.com");
        assertEquals("GET /v1/data/companies/www.acme.com", api.only().toString());
        assertEquals("acme.com", company.domain());
        assertEquals("Acme", company.name());
        assertEquals("Acme", company.names().get(0).name());
        assertEquals(Instant.parse("2026-10-01T00:00:00Z"), company.names().get(0).firstSeenAt());
        assertEquals("https://acme.com/careers", company.careersUrl());
        assertEquals("greenhouse", company.ats());
        assertEquals("complete", company.lastRead().status());
        assertEquals(Instant.parse("2026-11-17T00:00:00Z"), company.lastRead().lastCompleteAt());
        assertEquals(12, company.postings().open());
        assertEquals(30, company.postings().closed());
    }

    @Test
    void aCompanysPostingsTakeTheSameFiltersButNotTheDomainTwice() {
        api.ok("{\"postings\":[" + FakeApi.posting("p_1") + "],\"next_cursor\":\"n1\"}");
        api.ok("{\"postings\":[" + FakeApi.posting("p_2") + "],\"next_cursor\":null}");
        PostingListParams params = PostingListParams.builder().status("open").sort("last_seen_at").remote(false).build();
        List<Posting> postings = api.client().data().companies().postings("acme.com", params).toList();
        assertEquals(2, postings.size());
        assertEquals("GET /v1/data/companies/acme.com/postings?remote=false&status=open&sort=last_seen_at", api.received().get(0).toString());
        assertEquals("GET /v1/data/companies/acme.com/postings?remote=false&status=open&sort=last_seen_at&cursor=n1",
                api.received().get(1).toString());

        api.ok("{\"postings\":[],\"next_cursor\":null}");
        assertTrue(api.client().data().companies().postings("acme.com").toList().isEmpty());
        assertEquals("GET /v1/data/companies/acme.com/postings", api.last().toString());

        PostingListParams twice = PostingListParams.builder().companyDomain("other.com").build();
        assertThrows(IllegalArgumentException.class, () -> api.client().data().companies().postingsPage("acme.com", twice));
    }
}
