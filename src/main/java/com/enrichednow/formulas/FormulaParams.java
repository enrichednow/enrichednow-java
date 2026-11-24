// Generated from the API's formula parameter schemas. Do not edit by hand.
package com.enrichednow.formulas;

import java.util.List;
import java.util.Map;

/**
 * The parameters of one formula, as a type: one class each for every formula this version of the library knows
 * ({@link #FORMULA_IDS}), each with a builder. A formula that is not there takes its parameters as a plain
 * {@code Map}, as any formula does.
 */
public interface FormulaParams {
    /** Every formula this version of the library has a parameter type for. */
    List<String> FORMULA_IDS = List.of(
        "account-hiring-check",
        "account-research",
        "acquisition-universe",
        "ad-activity-trigger",
        "ad-creative-research",
        "agency-ad-mining",
        "agency-lists",
        "announcement-trigger",
        "app-intelligence",
        "award-teaming",
        "broken-link-prospects",
        "broker-directory",
        "business-for-sale-alerts",
        "candidate-reverse-marketing",
        "champion-tracker",
        "company-diligence",
        "competitor-price-tracker",
        "competitor-review-mining",
        "compound-signal-score",
        "contractor-license-watch",
        "crowdfunding-leads",
        "deal-site-monitor",
        "directory-to-leads",
        "ecommerce-panel",
        "enterprise-ats-sweep",
        "event-organizer-leads",
        "exhibitor-prospects",
        "footprint-prospects",
        "freelance-gig-feed",
        "funded-and-hiring",
        "hiring-signal-outbound",
        "hiring-velocity",
        "home-services-pros",
        "hotel-rate-shop",
        "influencer-discovery",
        "job-order-hunter",
        "job-seeker-targets",
        "knowledge-base-crawl",
        "landlord-agent-leads",
        "launch-batch-lists",
        "link-intersect",
        "listing-compliance",
        "literature-sweep",
        "local-business-leads",
        "long-open-roles",
        "lookalike-companies",
        "marketplace-flips",
        "menu-price-tracker",
        "out-of-stock-opportunity",
        "people-list-with-openers",
        "permit-status-watch",
        "permit-trade-leads",
        "price-seller-watch",
        "property-comps",
        "property-distress-signals",
        "rank-tracker",
        "reverse-sourcing",
        "review-mining",
        "review-velocity",
        "salary-benchmark",
        "sec-filing-monitor",
        "seller-identity",
        "shopify-niche-research",
        "social-sentiment",
        "stale-listings",
        "storefront-research",
        "str-underwriting",
        "supplier-discovery",
        "talent-sourcing",
        "tender-alerts",
        "tender-change-watch",
        "unlinked-mentions",
        "used-car-arbitrage",
        "vc-deal-sourcing",
        "web-rank-tracker",
        "wholesale-catalog-check");

    /**
     * The formula these parameters are for.
     *
     * @return its id, such as {@code job-order-hunter}
     */
    String formulaId();

    /**
     * The parameters as the API takes them: only those that were set.
     *
     * @return an unmodifiable map
     */
    Map<String, Object> toMap();
}
