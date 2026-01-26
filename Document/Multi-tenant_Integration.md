Option 1: Enhance EPBB Core to support PIS & multi-tenancy

Assessment:
After reviewing the current EPBB Core architecture and required enhancements, our conclusion is that this option is not feasible without disproportionate risk, re-engineering effort, and duplicated integration work.

Key considerations:
•	Multi-tenancy & active/active resiliency
EPBB Core was not designed for multi-tenant isolation or active/active operation. Achieving this would require fundamental changes to state management, data partitioning, and traffic routing.
•	Scalability & resilience requirements
Matching the resiliency and scale required for upcoming PIS demand would involve re-architecting large portions of the platform rather than incremental enhancement.
•	Delivery risk & velocity
The scope of change introduces significant delivery and operational risk, with limited confidence on timelines.
•	Long-term maintainability
Continued development would remain dependent on a small set of EPBB specialists, limiting scalability of engineering and support models.
•	Client adoption reality
Today, there are no active or committed clients consuming PISP v2 APIs on EPBB Core. Any investment made here would be speculative and would still require a future migration if/when clients need Global Platform capabilities.
•	Integration model mismatch (Salt-Edge vs Tink)
EPBB Core is currently built on Salt-Edge–based integration and does not consume Tink APIs directly. Supporting PIS on EPBB Core would therefore require:
•	Defining a new data and domain model aligned to Tink
•	Building and maintaining a new, parallel Tink integration within EPBB Core
This results in duplicated integration logic, increased maintenance cost, and architectural divergence from the Global Platform, where Tink integration is already native and standardized.

Conclusion:
While the current product is stable, enhancing EPBB Core to meet future PIS, multi-tenancy, and Tink-based integration requirements would be high-risk, cost-inefficient, and operationally complex, with no clear long-term sustainability.

⸻

Option 2: Build PIS on the V2 Global Platform

Assessment:
This option fully meets the technical and strategic requirements with significantly lower risk.

Key considerations:
•	Proven foundation
AISP has already been successfully delivered on the Global Platform for EMEA and MS, providing a validated base for PIS with minimal incremental work.
•	Native Tink integration
The Global Platform already consumes Tink APIs directly with aligned domain models, avoiding duplicated integration and reducing long-term maintenance overhead.
•	Built-in multi-tenancy & resilience
The Global Platform natively supports the active/active, scalable architecture required.
•	Delivery velocity & support model
Enables broader PBB engineering contribution and simplifies CAT/PROD support.
•	UI & extensibility
Better aligned with evolving UI/UX needs and consistent customization.
•	Cost & operational efficiency
Reduces duplicated infrastructure, integration logic, support overhead, and long-term maintenance cost.
•	Client onboarding strategy
Given that there are currently no clients live on EPBB Core for PISP v2, this presents an ideal opportunity to onboard the next new PIS client directly onto the Global Platform, avoiding a future migration and associated client, delivery, and operational risk.

⸻

Recommendation

Based on the above due diligence, Option 2 (Global Platform) is the only option that:
•	Meets the required technical capabilities
•	Avoids duplicated Salt-Edge vs Tink integration work
•	Minimizes delivery and operational risk
•	Avoids introducing a future client migration problem
•	Aligns with our long-term platform and vendor strategy

While we understand OBTS decommissioning is not yet formally scheduled, this recommendation stands independently of that timeline and is driven by architecture, scalability, integration alignment, and sustainability considerations.

⸻

Happy to walk through this in a session with Product and Tech together and align on next steps toward finalizing the SDD and downstream impact assessment.