# FoodBook Developer Guide

## **Acknowledgements**

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).

## **Product Scope** 

**Target user profile**:
- small food business owners in Singapore (e.g., home-based caterers, micro-F&B doing deliveries)


- manage many client addresses and recurring deliveries


- prefer fast CLI workflows over complex GUIs; type quickly; value minimal repetition


- often work solo or with 1 helper; need quick search/edit, duplicate prevention, and simple reporting


- store data locally


**Value proposition**: FoodBook provides a fast, reliable CLI to store, search, and manage clients and deliveries. Compared to notes/spreadsheets, it reduces admin time via quick add/edit/filter, validation & duplication warnings, status tracking, and simple reporting, improving delivery accuracy and day-to-day efficiency.

# User Stories

**Legend:** High (must have) – ``***``  |  Medium (nice to have) – ``**``  |  Low (later/considered) – ``*``

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
| ``**``  | food business owner  | export clients to a backup file                            | avoid data loss                              |
| ``**``  | food business owner  | sort clients alphabetically or by address                  | organize information better                  |
| ``*``   | food business owner  | history log per client ( past orders & delivery dates)     | keep track of loyal customers                |
| ``*``   | food business owner  | generate delivery reports                                  | analyze business patterns and growth         |

*Items marked ``*`` are considered but not required for MVP; keep if time permits.*

# Use Cases

_(For all use cases below, the System is FoodBook and the Actor is the user unless specified otherwise.)_

## UC01 – Add a client

**MSS:**

1. User enters:  
```add_client /n NAME /p PHONE /e EMAIL /u UNIT /zip POSTAL```
2. System saves the client.
4. System confirms success.  
**Use case ends.**

**Extensions**

- **2a. Any field invalid**  
→ 2a1. System shows specific error (e.g., “Postal code must be 6 digits only.”)  
→ Use case resumes at **Step 1**.

- **2a. Duplicate Client found**  
→ 3a1. System warns “Duplicate client found.”  
→ 3a2. User cancels or proceeds with a different name/phone.  
→ Use case resumes at **Step 1**.

## UC02 – Edit a client

**Precondition:** Client exists.

**MSS**

1. User searches the client:
```find_client /n Alice``` 
2. System shows matching clients.
3. User enters:  
```edit_client Alice Tan /p 98765432 /u #03-04 /zip 654321```
4. System validates new fields and updates the client.
5. System confirms success.  
**Use case ends.**

**Extensions**

- **2a. No matching client**  
→ System shows “Client not found.” **Use case ends.**

- **4a. New phone collides with another client’s phone**  
→ 4a1. System warns potential duplicate.  
→ Use case resumes at **Step 3**.

# UC03 – Add a delivery for a client

**Precondition:** Target client exists.

**MSS**

1. User confirms client (e.g., ```find_client /n Alice```)
and notes the exact name.
2. User enters:  
```add_delivery Alice Tan /d 20-09-2025 /t 1300 /r 3x Bento /$ 7```
3. System creates delivery with a unique ID.
5. System confirms success and displays the new delivery ID.  
**Use case ends.**

**Extensions**

- **2a. Client does not exist**  
→ System shows “Client does not exist.” **Use case ends.**

- **3a. Date in the past / invalid time / invalid price**  
→ System shows specific error and does not create the delivery.  
→ Use case resumes at **Step 2**.

# UC04 – View and mark deliveries for a day

**MSS**

1. User enters:  
```find_delivery 20-09-2025```
2. System lists all deliveries on that date with IDs and status.
3. User enters to mark a delivery completed:
```mark 67```
4. System updates status and confirms.  
**Use case ends.**

**Extensions**

- **1a. Invalid date format**  
→ System shows “Invalid date provided.” **Use case ends.**

- **2a. No deliveries found**  
→ System shows “No deliveries found.” **Use case ends.**

- **3a. Delivery ID not found**  
→ System shows “Delivery does not exist.” **Use case ends.**

# Non-Functional Requirements

## Performance
1. **Commands** should complete within **1 second** for up to **5,000 clients** and **20,000 deliveries**.
2. **Search/filter** should return results in **< 500 ms** on typical laptops.

## Usability
1. **Clear, actionable error messages** (field-specific).

## Reliability & Data Safety
1. **Auto-save** after every successful command.
2. **Undo** for most destructive operations.

## Portability (Constraint-Portable)
1. Runs on **Windows/macOS/Linux** with **Java 17+**.
2. **No OS-specific paths** required.

## Data Integrity & Validation
1. **Enforce input formats** (phone, email, unit, postal).
2. **Prevent accidental duplicates** via soft checks (same name + phone).

## Security & Privacy
1. **Store data locally**; **no external network** by default.

## Maintainability
1. Commands follow **AB3 command architecture**; **unit tests** for parsers and executors.



# Glossary

- **Client** — A customer who places orders.
- **Delivery** — A scheduled job tied to a client (date/time/remarks/price).
- **Delivery ID** — Unique identifier assigned by the system to a delivery.
- **High-priority** — Tag indicating preferred servicing order.
- **Capacity** — Daily/route limit to prevent overbooking.
- **Validation** — Input checks (format/range/consistency) performed before saving.

