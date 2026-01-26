# Appendix A – Product Requirements → SDD Mapping

## 1. Overview

This section maps the **Tink Multi-Tenancy Product Requirements** to the **Global PIS System Design Document (SDD)** to ensure:

- Full coverage of product intent
- Clear ownership between **JPM** and **Tink**
- Explicit confirmation of **in-scope vs out-of-scope** items
- Alignment for **contract renegotiation and cost reduction**

---

## 2. High-Level Mapping Summary

| Product Area | Product Requirement | SDD Section | Coverage |
|-------------|---------------------|-------------|----------|
| Deployment Model | Move to multi-tenant (Oxford) | §1, §2, §6 | ✅ |
| Vendor Dependency | Remove custom Tink builds | §1, §3, §6 | ✅ |
| PIS APIs | V2 PIS only | §3, §6 | ✅ |
| Settlement | JPM-owned settlement | §2, §5 | ✅ |
| Reconciliation | JPM-owned E2E reconciliation | §5 | ✅ |
| Refunds | Closed-loop refunds | §3, §5 | ✅ |
| UI & Redirect | JPM-hosted UX | §3, §6 | ✅ |
| Analytics | Product & API analytics | §3, §5 | ⚠️ Partial |
| Billing | Automated billing | §3 | ✅ |
| Regulatory Reporting | PSD2 reporting | §3 | ✅ |
| Migration | V1 → V2, Jersey → Oxford | §6 | ✅ |
| SDK | Out of scope | §6 | ✅ |

---

## 3. Detailed Mapping

### 3.1 Hosting & Multi-Tenancy

| Product Requirement | Description | SDD Mapping | Ownership |
|--------------------|-------------|-------------|-----------|
| Dedicated hosting removal | Decommission Jersey single-tenant | §1 Executive Summary | JPM |
| Multi-tenant only | Use Tink Oxford | §2 Architecture | Tink |
| Native APIs | No custom adapters | §3 Functional Requirements | JPM |

**Outcome:**  
The SDD explicitly supports removal of bespoke Tink infrastructure, enabling cost reduction and standardisation.

---

### 3.2 Vendor De-Customisation

| Product Requirement | Description | SDD Mapping | Ownership |
|--------------------|-------------|-------------|-----------|
| Remove JPM adaptor layer | Eliminate vendor-specific translation | §3, §6 | JPM |
| Native product consumption | Use Tink standard APIs only | §6 Tink Integration | Tink |
| URL & redirect ownership | JPM controls redirect lifecycle | §3 | JPM |

**Outcome:**  
Clear contract boundary: **Tink provides bank connectivity only; JPM owns orchestration**.

---

### 3.3 Payment Initiation (PIS)

| Product Requirement | Description | SDD Mapping | Ownership |
|--------------------|-------------|-------------|-----------|
| V2 PIS only | Decommission V1 | §3 Functional Requirements | JPM |
| Initiate payment | Create payment request | §3, §6 | Tink |
| Bank selection | Native Tink bank flow | §6 | Tink |
| Client abstraction | Stable JPM APIs | §3 | JPM |

**Outcome:**  
Clients experience no API change while backend migrates to multi-tenancy.

---

### 3.4 Settlement & Reconciliation (Key Strategic Shift)

| Product Requirement | Description | SDD Mapping | Ownership |
|--------------------|-------------|-------------|-----------|
| JPM-owned settlement | Internal confirmation of funds | §2, §5 | JPM |
| Treasury integration | JPM TS APIs | §2 Architecture | JPM |
| E2E tracking | Payment reference mapping | §5 | JPM |
| Client notifications | Webhooks & status APIs | §3 | JPM |

**Outcome:**  
Settlement is fully decoupled from Tink, removing vendor lock-in.

---

### 3.5 Payment Status & Events

| Product Requirement | Description | SDD Mapping | Ownership |
|--------------------|-------------|-------------|-----------|
| Real-time updates | Status notifications | §3 | JPM |
| Webhooks | Client subscriptions | §3 | JPM |
| Polling | GET payment status API | §3 | JPM |

**Outcome:**  
JPM becomes the system of record for the payment lifecycle.

---

### 3.6 Refunds & Payouts

| Product Requirement | Description | SDD Mapping | Ownership |
|--------------------|-------------|-------------|-----------|
| Closed-loop refunds | Linked to original PIS | §3, §5 | JPM |
| Partial refunds | Supported | §3 | JPM |
| Auto-refunds | Out of scope | §6 (Out of Scope) | — |

**Outcome:**  
Refunds remain compliant without introducing new vendor dependency.

---

### 3.7 UI, Consent & Redirects

| Product Requirement | Description | SDD Mapping | Ownership |
|--------------------|-------------|-------------|-----------|
| JPM consent screens | Localised, branded UX | §3 | JPM |
| Redirect control | Dynamic redirect URLs | §3 | JPM |
| Remove Tink UI | No hosted screens | §6 | JPM |

**Outcome:**  
Improved UX control and reduced Tink customisation costs.

---

### 3.8 Analytics, Reporting & Billing

| Product Requirement | Description | SDD Mapping | Status |
|--------------------|-------------|-------------|--------|
| Product analytics | Conversion & volume metrics | §5 | ⚠️ Partial |
| API metrics | Success/failure rates | §5 | ⚠️ Partial |
| Billing | Automated billing feeds | §3 | ✅ |
| Regulatory reporting | PSD2 compliance | §3 | ✅ |

**Action Required:**  
Enhance SDD with explicit analytics ownership, retention period, and merchant-level breakdown.

---

### 3.9 Migration & Decommissioning

| Product Requirement | Description | SDD Mapping | Ownership |
|--------------------|-------------|-------------|-----------|
| V1 → V2 migration | Client transition | §6 | JPM |
| Jersey → Oxford | Platform migration | §6 | Tink |
| AIS migration | MSFT & Global AIS | §6 | Tink |

**Outcome:**  
SDD supports phased migration without client disruption.

---

## 4. Explicit Out-of-Scope Confirmation

The following items are explicitly excluded from both Product Requirements and the SDD:

- SDK integration
- Auto-refund capabilities
- New V1 functionality
- Material changes to client-facing APIs

This ensures scope control and contractual clarity.

---

## 5. Final Alignment Statement

> The Global PIS SDD fully reflects the Tink Multi-Tenancy Product Requirements, with clear ownership boundaries, reduced vendor dependency, and a scalable, cost-efficient operating model.