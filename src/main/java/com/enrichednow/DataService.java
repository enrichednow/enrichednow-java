package com.enrichednow;

import com.enrichednow.models.Company;
import com.enrichednow.models.Posting;
import com.enrichednow.models.PostingDetail;

/**
 * Datasets: job postings and the companies they belong to, under {@code /v1/data}. Reached through
 * {@link EnrichedNowClient#data()}.
 *
 * <p>Datasets is a product of its own, read with the same API key. An account that has not been given it gets a
 * {@link com.enrichednow.errors.PermissionDeniedException}. Each record returned costs one data unit.
 */
public final class DataService {
    private final PostingService postings;
    private final CompanyService companies;

    DataService(Transport transport, Requests requests) {
        this.postings = new PostingService(transport, requests);
        this.companies = new CompanyService(transport, requests);
    }

    /** @return the job postings of the dataset */
    public PostingService postings() {
        return postings;
    }

    /** @return the companies of the dataset */
    public CompanyService companies() {
        return companies;
    }

    /** The job postings of the dataset. */
    public static final class PostingService {
        private final Transport transport;
        private final Requests requests;

        PostingService(Transport transport, Requests requests) {
            this.transport = transport;
            this.requests = requests;
        }

        /**
         * Every posting read from an applicant tracking system and matched to a company, newest first:
         * {@code GET /v1/data/postings}.
         *
         * @return the postings, fetched a page at a time as they are iterated
         */
        public PagedIterable<Posting> list() {
            return list(PostingListParams.none());
        }

        /**
         * The postings that match, newest first: {@code GET /v1/data/postings}.
         *
         * @param params the filters, the order and the page size
         * @return the postings, fetched a page at a time as they are iterated
         */
        public PagedIterable<Posting> list(PostingListParams params) {
            return new PagedIterable<>(() -> listPage(params));
        }

        /**
         * One page of the postings that match.
         *
         * @param params the filters, the order, the page size and, to continue an earlier list, the cursor
         * @return the page
         */
        public Page<Posting> listPage(PostingListParams params) {
            return transport.execute(requests.postings(params, null));
        }

        /**
         * One posting, with its description and history, whatever its provenance:
         * {@code GET /v1/data/postings/{id}}.
         *
         * @param postingId the posting's id
         * @return the posting
         * @throws com.enrichednow.errors.NotFoundException when the id names no posting
         */
        public PostingDetail get(String postingId) {
            return transport.execute(requests.posting(postingId));
        }
    }

    /** The companies of the dataset. */
    public static final class CompanyService {
        private final Transport transport;
        private final Requests requests;

        CompanyService(Transport transport, Requests requests) {
            this.transport = transport;
            this.requests = requests;
        }

        /**
         * Every company, by domain: {@code GET /v1/data/companies}.
         *
         * @return the companies, fetched a page at a time as they are iterated
         */
        public PagedIterable<Company> list() {
            return list(CompanyListParams.none());
        }

        /**
         * The companies that match, by domain: {@code GET /v1/data/companies}.
         *
         * @param params the filters and the page size
         * @return the companies, fetched a page at a time as they are iterated
         */
        public PagedIterable<Company> list(CompanyListParams params) {
            return new PagedIterable<>(() -> listPage(params));
        }

        /**
         * One page of the companies that match.
         *
         * @param params the filters, the page size and, to continue an earlier list, the cursor
         * @return the page
         */
        public Page<Company> listPage(CompanyListParams params) {
            return transport.execute(requests.companies(params, null));
        }

        /**
         * One company, by domain: {@code GET /v1/data/companies/{domain}}. Any host of the company's domain will do
         * ({@code www.acme.com}, {@code jobs.acme.com}).
         *
         * @param domain the company's domain
         * @return the company
         * @throws com.enrichednow.errors.NotFoundException when the dataset does not hold the company
         */
        public Company get(String domain) {
            return transport.execute(requests.company(domain));
        }

        /**
         * A company's postings: {@code GET /v1/data/companies/{domain}/postings}.
         *
         * @param domain the company's domain
         * @return the postings, fetched a page at a time as they are iterated
         */
        public PagedIterable<Posting> postings(String domain) {
            return postings(domain, PostingListParams.none());
        }

        /**
         * A company's postings that match, with the same filters, defaults and paging as the postings search.
         *
         * @param domain the company's domain
         * @param params the filters; {@code companyDomain} must not be set
         * @return the postings, fetched a page at a time as they are iterated
         */
        public PagedIterable<Posting> postings(String domain, PostingListParams params) {
            return new PagedIterable<>(() -> postingsPage(domain, params));
        }

        /**
         * One page of a company's postings.
         *
         * @param domain the company's domain
         * @param params the filters, the page size and, to continue an earlier list, the cursor
         * @return the page
         */
        public Page<Posting> postingsPage(String domain, PostingListParams params) {
            return transport.execute(requests.companyPostings(domain, params, null));
        }
    }
}
