# FoodBook — User Guide

FoodBook is a **desktop app for small food businesses to manage clients and deliveries**. While it has a GUI, most interactions use a fast **Command Line Interface (CLI)** so you can work quickly during busy hours.

---

## Table of Contents

1. [Introduction](#introduction)
2. [Quick Start](#quick-start)
3. [How to Read This Guide](#how-to-read-this-guide)
4. [UI at a Glance](#ui-at-a-glance)
5. [Features](#features)
    - [Clients](#clients)
    - [Deliveries](#deliveries)
    - [General Utilities](#general-utilities)
6. [Data Files & Auto-Save](#data-files--auto-save)
7. [FAQ](#faq)
8. [Troubleshooting & Known Issues](#troubleshooting--known-issues)
9. [Command Summary](#command-summary)
10. [Glossary](#glossary)
11. [Attributions](#attributions)

---

## Introduction

### Target Users & Assumptions

**Who this is for:**  
Home-based chefs, micro-F&B owners, and small catering teams who need a **simple, reliable way** to track clients and deliveries.

**Assumptions about prior knowledge:**

- You are comfortable typing commands.
- You know basic date/time formats: `d/M/yyyy` (e.g., `4/11/2025`) and **24-hour time** without colons (e.g., `1430`).
- You can run a desktop Java app on Windows/macOS/Linux.

### What You'll Accomplish with FoodBook

- Build a clean **Client list** with contact info and tags.
- Schedule and track **Deliveries** linked to clients.
- **Mark** completed deliveries and **find** items by client/date.
- Work **keyboard-first** for speed; the GUI updates automatically.

---

## Quick Start

1. **Install Java 17 or newer.**
2. **Run the app** from the project root:

```bash
./gradlew clean run
```

FoodBook launches with sample data.

3. Type `help` and press Enter to open the help window.

4. Try these first:
    - `list` — shows all clients
    - `find_delivery d/25/12/2024` — shows deliveries for that date
    - `help` — quick reference

**Tip:** FoodBook auto-saves after every successful command. No manual save required.

---

## How to Read This Guide

- Words in `UPPER_CASE` are placeholders — replace them with your values.
- Items in `[square brackets]` are optional.
- Parameters can be in any order.
- **Expected Output** shows what you will typically see after a successful command.
- **Warnings** flag common mistakes (formatting, missing fields, etc.).
- **Tips** suggest faster or safer ways to use a feature.

### Date & Time Formats

- **Date:** `d/M/yyyy` (e.g., `4/11/2025` or `04/11/2025` — leading zeros optional)
- **Time:** 24-hour format without colon, 4 digits (e.g., `1430`, `0800`)

---

## UI at a Glance

- **Menu Bar / Help:** Open help or exit.
- **Command Box:** Type commands and press Enter.
- **Result Display:** Success/error messages.
- **List Panels:** Show Clients and Deliveries.
- **Cards:** Each client/delivery is presented as a card with key fields and tag pills.
- **Delivery cards show:** Delivery ID, Client name, Date, Time, Price, Remarks, Tag(s), and `Delivered: True/False`.

**Note on tag colours:**  
Special colours are used for `Personal` and `Corporate` tags. All other tags use a default style.

---

## Features

All commands are case-sensitive for their prefixes (e.g., `n/`, `p/`, `d/`, `tm/`, `dt/`).  
Use `help` anytime to view a quick command reference.

---

### Clients

#### Add Client — `add`

Create a client with contact and address details.

**Format:**
```
add n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]...
```

**Examples:**
```
add n/May Chen p/81234567 e/mayc@example.com a/Blk 123 #05-12, 560123 t/regular
add n/Adam Ong p/69881234 e/adam.ong@work.sg a/22 River Valley Rd, 238255
```

**Expected Output:**  
A success message and a new client card appear in the list.

**Warnings:**
- Phone should be 8 digits.
- Postal codes in addresses should be 6 digits (if provided).
- Duplicate detection may warn if name + contact details already exist.

**Tips:**  
Use short, meaningful `t/` tags (e.g., `t/VIP`, `t/weekly`).

---

#### Edit Client — `edit_client`

Update any subset of fields for an existing client.

**Format:**
```
edit_client CLIENT_NAME [n/NEW_NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]...
```

**Example:**
```
edit_client May Chen p/95551234 a/11 Holland Dr #02-15, 270011 t/weekly t/vegan
```

**Expected Output:**  
A success message with updated fields. Existing deliveries remain linked to this client.

**Warnings:**
- `CLIENT_NAME` must match an existing client's name exactly.
- At least one field to change must be provided.

**Tips:**  
Keep `t/` tags consistent across clients to make searching easier.

---

#### Delete Client — `delete`

Remove a client by index.

**Format:**
```
delete INDEX
```

**Example:**
```
delete 2
```

**Expected Output:**  
Client removed from the list with a confirmation message.

**Warnings:**
- Irreversible. Ensure the client has no required records you still need.
- The index refers to the number shown in the displayed client list.

---

#### Find Client — `find`

Search clients by name keywords.

**Format:**
```
find KEYWORD [MORE_KEYWORDS]
```

**Examples:**
```
find may
find adam ong
```

**Expected Output:**  
List updates to only the clients whose names contain any of the keywords.

**Tips:**  
Search is case-insensitive. Use `list` to show all clients again.

---

### Deliveries

#### Add Delivery — `add_delivery`

Create a delivery linked to an existing client.

**Format:**
```
add_delivery n/CLIENT_NAME d/DATE tm/TIME r/REMARKS c/COST [t/TAG]
```

**Examples:**
```
add_delivery n/May Chen d/4/11/2025 tm/1430 c/28.50 r/2x laksa, leave at reception t/Personal
add_delivery n/Adam Ong d/4/11/2025 tm/1800 c/120.00 r/Company buffet t/Corporate
```

**Expected Output:**  
A success message and a new Delivery ID appear. Delivery card shows client, date, time, price, remarks, tag, and `Delivered: False`.

**Warnings:**
- Client must exist (use exact name from client list).
- Date must be in `d/M/yyyy` format, time in `HHmm` format.
- Cost must be non-negative.
- All fields (`n/`, `d/`, `tm/`, `r/`, `c/`) are required.

**Tips:**  
Use delivery tags (`t/`) to group runs (e.g., `t/Morning`, `t/Islandwide`) and business type (`t/Personal`, `t/Corporate`).

---

#### Edit Delivery — `edit_delivery`

Modify one or more fields of an existing delivery.

**Format:**
```
edit_delivery DELIVERY_ID [n/CLIENT_NAME] [d/DATE tm/TIME] [c/COST] [r/REMARKS] 
```

**Example:**
```
edit_delivery 1032 tm/1515 r/Customer requested later pickup
```

**Expected Output:**  
Delivery card updates with the new fields.

**Warnings:**
- `DELIVERY_ID` must exist (shown on delivery card).
- At least one field to edit is required.
- Date and time must be provided together (both `d/` and `tm/`).

---

#### Delete Delivery — `delete_delivery`

Remove a delivery by ID.

**Format:**
```
delete_delivery DELIVERY_ID
```

**Expected Output:**  
Delivery removed from the list with a confirmation message.

**Warnings:**  
Irreversible. Consider backups before cleanup.

---

#### Mark/Unmark — `mark` / `unmark`

Toggle delivery completion status.

**Formats:**
```
mark DELIVERY_ID
unmark DELIVERY_ID
```

**Expected Output:**  
The delivery card shows `Delivered: True` (or `False`) and a confirmation message.

**Tips:**  
Use `find_delivery` (today) → `mark` as you finish each stop to keep a live run-sheet.

---

#### Find Delivery (by date) — `find_delivery`

List deliveries on a specific date.

**Format:**
```
find_delivery d/DATE
```

**Example:**
```
find_delivery d/4/11/2025
```

**Expected Output:**  
All deliveries for that date with ID, client, time, price, remarks, tags.

**Tips:**  
Use with `mark` during your day to track progress. Use `list_delivery` to show all deliveries again.

---

### General Utilities

- `help` — Opens a window with a quick reference.
- `list` — Shows all clients (useful after searching).
- `list_delivery` — Shows all deliveries.
- `clear` — Clears all entries (use with caution).
- `exit` — Quits the app.

**Keyboard flow:**  
Type a command → Enter → Read the result → Arrow keys to recall previous commands (system-dependent).

---

## Data Files & Auto-Save

- FoodBook stores data locally as JSON in `[JAR location]/data/addressbook.json`.
- Auto-save runs after every successful command.
- If you manually edit the data and the format becomes invalid, the app may discard all data and start with an empty file on next launch.

**Caution:** Only edit JSON directly if you are comfortable with the schema. Always keep backups.

---

## FAQ

**Q. Can I use keyboard only?**  
Yes. The app is designed for fast CLI input. The GUI updates as you type commands.

**Q. Can I colour-code tags?**  
Yes. `Personal` and `Corporate` delivery tags have special colours. Other tags appear in a default style.

**Q. What happens if I type an invalid date?**  
The app rejects the command and shows an error with the expected format (`d/M/yyyy`).

**Q. Can I undo a mistaken delete?**  
Deletions are irreversible within the app. Keep regular backups if you need recovery.

---

## Troubleshooting & Known Issues

**App opens off-screen after using a second monitor:**  
Delete `preferences.json` (the window will reset to defaults on next launch).

**Help window appears once:**  
If minimized, run `help` again and restore the minimized window (a new one will not open).

**Nothing happens after pressing Enter:**  
Check that the Command Box is focused. Verify command prefixes (`n/`, `p/`, `d/`, `tm/`, `dt/`, etc.) and date/time formats.

---

## Command Summary

| Action | Format |
|--------|--------|
| Add Client | `add n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]...` |
| Edit Client | `edit_client CLIENT_NAME [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]...` |
| Delete Client | `delete INDEX` |
| Find Client | `find KEYWORD [MORE_KEYWORDS]` |
| List Clients | `list` |
| Add Delivery | `add_delivery n/CLIENT_NAME d/DATE tm/TIME r/REMARKS c/COST [dt/TAG]` |
| Edit Delivery | `edit_delivery DELIVERY_ID [n/CLIENT_NAME] [d/DATE tm/TIME] [c/COST] [r/REMARKS] [dt/TAG]` |
| Delete Delivery | `delete_delivery DELIVERY_ID` |
| Mark/Unmark | `mark DELIVERY_ID` / `unmark DELIVERY_ID` |
| Find Delivery | `find_delivery d/DATE` |
| List Deliveries | `list_delivery` |
| Help | `help` |
| Clear | `clear` |
| Exit | `exit` |

---

## Glossary

- **Client Name:** The full name of a client as stored in FoodBook. Must match exactly for commands like `edit_client`.
- **Delivery ID:** Unique identifier shown on the delivery card; use it for edit/mark/delete operations.
- **Tag:** A label you attach to clients (using `t/`) or deliveries (using `dt/`). Examples: `Personal`, `Corporate`, `VIP`, `MorningRun`.
- **Card:** The visual block showing a client or delivery in the list panel.
- **Index:** The number shown next to each client in the displayed list (used for `delete` command).

---

## Attributions

Built with Java, JavaFX, Gradle, Jackson, and JUnit.  
Based on the AddressBook-Level3 (AB3) educational codebase and adapted for FoodBook's delivery workflow.