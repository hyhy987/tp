# FoodBook — User Guide
FoodBook is a **desktop app for small food businesses to manage clients and deliveries**. While it has
a GUI, most interactions use a fast **Command Line Interface (CLI)** so you can work quickly during busy hours.


***


## Table of Contents


1. [Introduction](#introduction)
2. [Quick Start](#quick-start)
3. [How to Read This Guide](#how-to-read-this-guide)
4. [UI at a Glance](#ui-at-a-glance)
5. [Features](#features)
- [Clients](#clients)
    - [Add Client — `add_client`](#add-client--add_client)
    - [Edit Client — `edit_client`](#edit-client--edit_client)
    - [Delete Client — `delete_client`](#delete-client--delete_client)
    - [Find Client — `find_client`](#find-client--find_client)
- [Deliveries](#deliveries)
    - [Add Delivery — `add_delivery`](#add-delivery--add_delivery)
    - [Edit Delivery — `edit_delivery`](#edit-delivery--edit_delivery)
    - [Delete Delivery — `delete_delivery`](#delete-delivery--delete_delivery)
    - [Mark/Unmark — `mark` / `unmark`](#markunmark--mark--unmark)
    - [Find Delivery (by date) — `find_delivery`](#find-delivery-by-date--find_delivery)
- [General Utilities](#general-utilities)
6. [Data Files \& Auto-Save](#data-files--auto-save)
7. [FAQ](#faq)
8. [Troubleshooting \& Known Issues](#troubleshooting--known-issues)
9. [Command Summary](#command-summary)
10. [Glossary](#glossary)
11. [Attributions](#attributions)


***


## Introduction


### Target Users \& Assumptions


**Who this is for:**
Home-based chefs, micro-F\&B owners, and small catering teams who need a **simple, reliable way** to track clients and deliveries.


**Assumptions about prior knowledge:**


- You are comfortable typing commands.
- You know basic date/time formats: `DD/MM/YYYY` and **24-hour time** (e.g., `1730`).
- You can run a desktop Java app on Windows/macOS/Linux.




### What You’ll Accomplish with FoodBook


- Build a clean **Client list** with contact info and tags.
- Schedule and track **Deliveries** linked to clients.
- **Mark** completed deliveries and **find** items by client/date.
- Work **keyboard-first** for speed; the GUI updates automatically.


***


## Quick Start


1. **Install Java 17 or newer.**
2. **Run the app** from the project root:


```bash
./gradlew clean run
```


FoodBook launches with sample data.


Type `help` and press Enter to open the help window.


Try these first:


```
list_clients — shows all clients


find_delivery d/TODAYSDATE — shows today’s deliveries


help — quick reference
```


**Tip:** FoodBook auto-saves after every successful command. No manual save required.


***


## How to Read This Guide


- Words in **UPPER_CASE** are placeholders — replace them with your values.
- Items in `[square brackets]` are optional.
- Parameters can be in any order.
- **Expected Output** shows what you will typically see after a successful command.
- **Warnings** flag common mistakes (formatting, missing fields, etc.).
- **Tips** suggest faster or safer ways to use a feature.


***


## Date \& Time


- Date: `DD/MM/YYYY` (e.g., `04/11/2025`)
- Time: 24-hour format without colon (e.g., `1430`)


***


## UI at a Glance


- **Menu Bar / Help:** Open help or exit.
- **Command Box:** Type commands and press Enter.
- **Result Display:** Success/error messages.
- **List Panels:** Show Clients and Deliveries.
- **Cards:** Each client/delivery is presented as a card with key fields and tag pills.
- **Delivery cards show:** Delivery ID, Client name, Date, Time, Price, Remarks, Tag(s), and Delivered: True/False.


**Note on tag colours:**
Special colours are used for Personal and Corporate tags. All other tags use a default style.


***


## Features


All commands are **case-sensitive** for their prefixes (e.g., `n/`, `p/`, `d/`, `t/`).
Use `help` anytime to view a quick command reference.


***


### Clients


#### Add Client — `add_client`


Create a client with contact and address details.


**Format:**


```bash
add_client n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]...
```


**Examples:**


```bash
add_client n/May Chen p/81234567 e/mayc@example.com a/Blk 123 #05-12, 560123 t/regular
add_client n/Adam Ong p/69881234 e/adam.ong@work.sg a/22 River Valley Rd, 238255
```


**Expected Output:**
A success message and a new client card appear in the list.


**Warnings:**


- Phone should be 8 digits.
- Postal codes in addresses should be 6 digits (if provided).
- Duplicate detection may warn if name + contact details already exist.


**Tips:**
Use short, meaningful `t/` tags (e.g., `t/VIP`, `t/weekly`).


***


#### Edit Client — `edit_client`


Update any subset of fields for an existing client.


**Format:**


```bash
edit_client i/CLIENT_ID [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]...
```


**Example:**


```bash
edit_client i/7 p/95551234 a/11 Holland Dr #02-15, 270011 t/weekly t/vegan
```


**Expected Output:**
A success message with updated fields. Existing deliveries remain linked to this client.


**Warnings:**


- `i/CLIENT_ID` must refer to a valid existing client.
- At least one field to change must be provided.


**Tips:**
Keep `t/` tags consistent across clients to make searching easier.


***


#### Delete Client — `delete_client`


Remove a client by id.


**Format:**


```bash
delete_client i/CLIENT_ID
```


**Expected Output:**
Client removed from the list with a confirmation message.


**Warnings:**


- Irreversible. Ensure the client has no required records you still need.
- Depending on your data policy, associated info may be removed. Back up if unsure.


***


#### Find Client — `find_client`


Search clients by name, phone, or email. Matches are case-insensitive for text fields and partial for name/email.


**Format:**


```bash
find_client [n/NAME_KEYWORD] [p/PHONE] [e/EMAIL]
```


**Examples:**


```bash
find_client n/may
find_client p/81234567
find_client e/@work.sg
```


**Expected Output:**
List updates to only the clients that match any of the provided fields.


**Tips:**
Combine filters for narrower results, e.g., `find_client n/adam e/@work.sg`.


***


### Deliveries


#### Add Delivery — `add_delivery`


Create a delivery linked to an existing client.


**Format:**


```bash
add_delivery c/CLIENT_ID d/DD/MM/YYYY t/TIME24H [$/PRICE] [r/REMARKS] [t/TAG]...
```


**Examples:**


```bash
add_delivery c/7 d/04/11/2025 t/1430 $/28.50 r/2x laksa, leave at reception t/Personal
add_delivery c/12 d/04/11/2025 t/1800 $/120 r/Company buffet t/Corporate t/VIP
```


**Expected Output:**
A success message and a new Delivery ID (e.g., Delivery ID: 1032) appear.


Delivery card shows client, date, time, price, remarks, tag(s), and Delivered: False.


**Warnings:**


- Client must exist (`c/CLIENT_ID`).
- Date/time must follow the specified formats.
- Invalid numbers (e.g., negative price) are rejected.


**Tips:**
Use tags to group runs (e.g., `t/Morning`, `t/Islandwide`) and business type (`t/Personal`, `t/Corporate`) for quick scanning.


***


#### Edit Delivery — `edit_delivery`


Modify one or more fields of an existing delivery.


**Format:**


```bash
edit_delivery i/DELIVERY_ID [d/DD/MM/YYYY] [t/TIME24H] [$/PRICE] [r/REMARKS] [t/TAG]...
```


**Example:**


```bash
edit_delivery i/1032 t/1515 r/Customer requested later pickup
```


**Expected Output:**
Delivery card updates with the new fields.


**Warnings:**


- `i/DELIVERY_ID` must exist.
- At least one field to edit is required.


***


#### Delete Delivery — `delete_delivery`


**Format:**


```bash
delete_delivery i/DELIVERY_ID
```


**Expected Output:**
Delivery removed from the list with a confirmation message.


**Warnings:**


- Irreversible. Consider using exports/backups before cleanup.


***


#### Mark/Unmark — `mark` / `unmark`


Toggle delivery completion.


**Formats:**


```bash
mark DELIVERY_ID
unmark DELIVERY_ID
```


**Expected Output:**
The delivery card shows Delivered: True (or False) and a confirmation message.


**Tips:**
Use `find_delivery` (today) → mark as you finish each stop to keep a live run-sheet.


***


#### Find Delivery (by date) — `find_delivery`


List deliveries on a specific date.


**Format:**


```bash
find_delivery d/DD/MM/YYYY
```


**Example:**


```bash
find_delivery d/04/11/2025
```


**Expected Output:**
All deliveries for that date with ID, client, time, price, remarks, tags.


**Tips:**
Use with `mark` during your day to track progress.


***


### General Utilities


- `help` — Opens a window with a quick reference.
- `list_clients` — Shows all clients (useful after searching).
- `clear` — Clears lists (if available in your build).
- `exit` — Quits the app.


Keyboard flow:
Type a command → Enter → Read the result → Arrow keys to recall previous commands (system-dependent).


***


## Data Files \& Auto-Save


- FoodBook stores data locally (JSON-based).
- Auto-save runs after every successful command.
- If you manually edit the data and the format becomes invalid, the app may reset that file to a safe state on next launch.
- **Caution:** Only edit JSON directly if you are comfortable with the schema. Always keep backups.


***


## FAQ


**Q. Can I use keyboard only?**
Yes. The app is designed for fast CLI input. The GUI updates as you type commands.


**Q. Can I colour-code tags?**
Yes. Personal and Corporate have special colours. Other tags appear in a default style.


**Q. What happens if I type an invalid date?**
The app rejects the command and shows an error with the expected format.


**Q. Can I undo a mistaken delete?**
Deletions are irreversible within the app. Keep regular backups if you need recovery.


***


## Troubleshooting \& Known Issues


- **App opens off-screen after using a second monitor:**
  Delete `preferences.json` (the window will reset to defaults on next launch).
- **Help window appears once:**
  If minimized, run `help` again and restore the minimized window (a new one will not open).
- **Nothing happens after pressing Enter:**
  Check that the Command Box is focused.
  Verify command prefixes (`n/`, `p/`, `d/`, `t/`, etc.) and date/time formats.


***


## Command Summary


| Action | Format                                                                                    |
| :-- |:------------------------------------------------------------------------------------------|
| Add Client | `add_client n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]...`                                  |
| Edit Client | `edit_client i/CLIENT_ID [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]...`             |
| Delete Client | `delete_client i/CLIENT_ID`                                                               |
| Find Client | `find_client [n/KEYWORD] [p/PHONE] [e/EMAIL]`                                             |
| Add Delivery | `add_delivery c/CLIENT_ID d/DD/MM/YYYY t/TIME24H [$/PRICE] [r/REMARKS] [t/TAG]...`        |
| Edit Delivery | `edit_delivery i/DELIVERY_ID [d/DD/MM/YYYY] [t/TIME24H] [$/PRICE] [r/REMARKS] [t/TAG]...` |
| Delete Delivery | `delete_delivery i/DELIVERY_ID`                                                           |
| Mark/Unmark | `mark DELIVERY_ID` / `unmark DELIVERY_ID`                                                 |
| Find Delivery (by date) | `find_delivery d/DD/MM/YYYY`                                                              |
| Help / Exit / Clear | `help` / `exit` / `clear` (if available)                                                  |




***


## Glossary


- **Client ID / Delivery ID:** Unique identifier shown on the card; use it for edit/mark/delete.
- **Tag:** A label you attach to clients/deliveries (e.g., Personal, Corporate, VIP, MorningRun).
- **Card:** The visual block showing a client or delivery in the list panel.


***


## Attributions


- Built with Java, JavaFX, Gradle, Jackson, and JUnit.
- Based on the AddressBook-Level3 (AB3) educational codebase and adapted for FoodBook’s delivery workflow.


***
