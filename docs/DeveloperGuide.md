---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# FoodBook Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org)._

--------------------------------------------------------------------------------------------------------------------

## **Product Scope** 

**Target user profile**:
- small food business owners in Singapore (e.g., home-based caterers, micro-F&B doing deliveries)


- manage many client addresses and recurring deliveries


- prefer fast CLI workflows over complex GUIs; type quickly; value minimal repetition


- often work solo or with 1 helper; need quick search/edit, duplicate prevention, and simple reporting


- store data locally


**Value proposition**: FoodBook provides a fast, reliable CLI to store, search, and manage clients and deliveries. Compared to notes/spreadsheets, it reduces admin time via quick add/edit/filter, validation & duplication warnings, status tracking, and simple reporting, improving delivery accuracy and day-to-day efficiency.

--------------------------------------------------------------------------------------------------------------------

# User Stories (GFM)

**Legend:** High (must have) – ``***``  |  Medium (nice to have) – ``**``  |  Low (later/considered) – ``*``

> Use the exact Markdown markers above when editing: e.g., type ``**bold**`` for **bold**, ``*italic*`` for *italic*, and use backticks `` ` `` to show the literal symbols like ``***`` in text.

| Priority | As a …              | I want to …                                               | So that I can …                              |
|:-------:|----------------------|------------------------------------------------------------|----------------------------------------------|
| ``***`` | food business owner  | add a client with name/phone/email/unit/postal             | keep accurate contact/address records        |
| ``***`` | food business owner  | edit a client’s details                                    | keep information up-to-date                  |
| ``***`` | food business owner  | delete a client                                            | remove outdated entries                      |
| ``***`` | food business owner  | add a delivery (date/time/remarks/price, linked to client) | schedule jobs and avoid forgetting           |
| ``***`` | food business owner  | mark/unmark a delivery as completed                        | see what’s pending vs done                   |
| ``***`` | food business owner  | edit a delivery                                            | change delivery details dynamically          |
| ``***`` | food business owner  | delete a delivery                                          | Remove dud deliveries                        |
| ``***`` | food business owner  | search clients by name/phone/email                         | find a client quickly                        |
| ``***`` | food business owner  | search deliveries by date                                  | see all jobs for a day                       |
| ``***`` | food business owner  | import client data from a file                             | set up quickly from existing lists           |
| ``**``  | food business owner  | undo a recent change                                       | recover from mistakes                        |
| ``**``  | food business owner  | tag clients as corporate or high-priority                  | prioritise fulfilment                        |
| ``**``  | food business owner  | repeat/schedule weekly deliveries                          | avoid recreating recurring orders            |
| ``**``  | food business owner  | merge duplicate orders                                     | keep my records clean                        |
| ``**``  | food business owner  | search for client by address and or part of it             | observe customer base by region              |
| ``**``  | food business owner  | add private notes to each delivery                         | keep track of special requests by clients    |
| ``**``  | food business owner  | create command aliases                                     | so that frequent actions become faster       |
| ``**``  | food business owner  | export clients to a backup file                            | avoid data loss                              |
| ``**``  | food business owner  | sort clients alphabetically or by address                  | organize information better                  |
| ``*``   | food business owner  | history log per client ( past orders & delivery dates)     | keep track of loyal customers                |
| ``*``   | food business owner  | change log per client (who/what/when)                      | retain an audit trail                        |
| ``*``   | food business owner  | set capacity limits per region per day                     | prevent overbooking on those days            |
| ``*``   | food business owner  | generate delivery reports                                  | analyze business patterns and growth         |

*Items marked ``*`` are considered but not required for MVP; keep if time permits.*
                                           |