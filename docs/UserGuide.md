# FoodBook — User Guide

FoodBook is a **desktop app for small food businesses to manage clients and deliveries**. While it has a GUI, most interactions use a fast **Command Line Interface (CLI)** so you can work quickly during busy hours.

---

## Table of Contents

- [Quick Start](#quick-start)
- [Features](#features)
  - [Viewing help : `help`](#viewing-help--help)
  - [Clients](#clients)
    - [Listing all clients : `list_client`](#listing-all-clients--list_client)
    - [Adding a client : `add_client`](#adding-a-client--add_client)
    - [Editing a client : `edit_client`](#editing-a-client--edit_client)
    - [Locating clients : `find_client`](#locating-clients--find_client)
    - [Deleting a client : `delete_client`](#deleting-a-client--delete_client)
  - [Deliveries](#deliveries)
    - [Listing all deliveries : `list_delivery`](#listing-all-deliveries--list_delivery)
    - [Adding a delivery : `add_delivery`](#adding-a-delivery--add_delivery)
    - [Editing a delivery : `edit_delivery`](#editing-a-delivery--edit_delivery)
    - [Marking / Unmarking : `mark` / `unmark`](#marking--unmarking--mark--unmark)
    - [Locating deliveries : `find_delivery`](#locating-deliveries--find_delivery)
    - [Deleting a delivery : `delete_delivery`](#deleting-a-delivery--delete_delivery)
  - [Revenue : `list_revenue`](#revenue--list_revenue)
  - [Undo : `undo`](#undo--undo)
  - [Clearing all entries : `clear`](#clearing-all-entries--clear)
  - [Exiting the program : `exit`](#exiting-the-program--exit)
- [Saving the data](#saving-the-data)
- [Editing the data file](#editing-the-data-file)
- [FAQ](#faq)
- [Known issues](#known-issues)
- [Command Summary](#command-summary)

---

## Quick Start

1. **Install Java 17 or newer.**
2. Place `foodbook.jar` in your preferred folder.
3. Run the app:
   ```bash
   java -jar foodbook.jar
   ```
   FoodBook launches with sample data.
4. Try:
   - `help` — quick reference
   - `list_client` — list all clients
   - `list_delivery` — list all deliveries


---

## Features

### Viewing help : `help`

Shows the help window with a quick reference.

**Format:** `help`

---

## Clients

### Listing all clients : `list_client`

Displays all clients currently stored in FoodBook. Each client is shown with their name, phone number, email, address, and any tags. This provides a complete overview of your client database.

**Format:** `list_client`

**What you'll see:**
- Client name and contact information
- Physical address for deliveries  
- Tags for easy categorization (e.g., "regular", "corporate")

![List](images/list_client.png)

---

### Adding a client : `add_client`

Adds a new client to FoodBook with their contact and delivery information. Each client must have a unique name - you cannot add two clients with the same name.

**Format:**
```
add_client n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]
```

**Parameter Details:**
- `n/NAME`: Full client name (must be unique, cannot be empty)
- `p/PHONE`: Contact number (minimum 3 digits, numbers only)
- `e/EMAIL`: Valid email address (must contain @ symbol)
- `a/ADDRESS`: Full delivery address (cannot be empty)  
- `t/TAG`: Optional label for categorization (alphanumeric only)

**Examples:**
```
add_client n/May Chen p/81234567 e/mayc@example.com a/Blk 123 #05-12, 560123 t/regular
add_client n/Acme Catering p/65123456 e/sales@acme.com a/10 Science Park Dr t/corporate
```

![add](images/add_client.png)

**Important Notes:**
- Client names must be unique across your database
- Phone numbers must be at least 3 digits long (no letters or special characters)
- Email addresses are validated for proper format
- Tags help organize clients but are completely optional
- All fields except tags are required

---

### Editing a client : `edit_client`

Updates information for an existing client in FoodBook. You identify the client by their current name, then specify which fields to update. Any linked deliveries will automatically reflect the client's updated information.

**Format:**
```
edit_client CURRENT_NAME [n/NEW_NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]
```

**How it works:**
- Specify the client's **exact current name** (case-sensitive)
- Provide at least one field to update
- Only the fields you specify will be changed
- All existing linked deliveries will reflect the updated information

**Example:**
```
edit_client May Chen p/95551234 a/11 Holland Dr #02-15, 270011 t/URGENT
```

![List](images/edit_client.png)

**Important Notes:**
- The current name must match **exactly** (including spaces and capitalization)
- At least one optional field must be provided
- If changing the name, the new name must not already exist
- When editing tags, existing tags are completely replaced (not added to)
- You can remove all tags by typing `t/` without any value
- Phone and email validation rules still apply

---

### Locating clients : `find_client`

Searches for clients based on name keywords, phone numbers, or email addresses. You can search by any combination of these fields, and clients matching **any** of the provided criteria will be shown.

**Format:**
```
find_client [n/NAME_KEYWORDS] [p/PHONE] [e/EMAIL]
```

**Search Behavior:**
- `n/NAME_KEYWORDS`: Searches within client names (case-insensitive, partial matches)
- `p/PHONE`: Searches for exact phone number matches
- `e/EMAIL`: Searches within email addresses (partial matches allowed)
- At least one search field must be provided

**Examples:**
```
find_client n/May                   // Finds "May Chen", "Maya Restaurant", etc.
find_client p/81234567              // Finds exact phone match
find_client e/@acme.com             // Finds emails containing "@acme.com"
find_client n/chen e/mayc           // Finds clients with "chen" in name OR "mayc" in email
```

![find](images/find_client.png)

**Search Tips:**
- Name searches are case-insensitive and match partial words
- Phone searches require exact matches of the full number
- Email searches can match domains (e.g., "@gmail.com") or usernames
- Results show all clients matching any of your criteria

---

### Deleting a client : `delete_client`

Permanently removes a client from FoodBook. **This action also deletes all deliveries associated with that client** - this cannot be undone except by using the `undo` command.

**Format:**
```
delete_client NAME
```

**What gets deleted:**
- The client's complete record (name, phone, email, address, tags)
- **All past deliveries** linked to this client
- All revenue records from those deliveries

**Example:**
```
delete_client Acme Pte Ltd
```

![delete](images/after_delete_client.png)

**Important Warnings:**
- **This action is irreversible** (except via `undo`)
- The client name must match **exactly** (case-sensitive)
- All associated delivery records will be permanently lost
- Revenue calculations will be affected if you delete clients with completed deliveries
- Consider using `undo` immediately if you delete the wrong client

**Before deleting:**
- Double-check the client name spelling and capitalization
- Consider if you want to keep delivery history for business records
- Use `find_delivery n/CLIENT_NAME` to see what deliveries will be lost

---

## Deliveries

### Listing all deliveries : `list_delivery`

Displays all delivery records in FoodBook, showing comprehensive information about each delivery including client details, schedule, cost, and completion status. This gives you a complete overview of your delivery operations.

**Format:** `list_delivery`

**What you'll see:**
- Unique delivery ID for easy reference
- Client name and associated client information
- Delivery date and time in a clear format
- Delivery cost and any special remarks
- Completion status (Delivered: True/False)

![List Delivery](images/list_delivery.png)

---

### Adding a delivery : `add_delivery`

Creates a new delivery record linked to an existing client. The client must already exist in your FoodBook before you can add a delivery for them. Each delivery is automatically assigned a unique ID for easy tracking.

**Format:**
```
add_delivery n/CLIENT_NAME d/DATE tm/TIME c/COST r/REMARKS [t/TAG]
```

**Parameter Details:**
- `n/CLIENT_NAME`: Must match an existing client's name exactly (case-sensitive)
- `d/DATE`: Delivery date in `d/M/yyyy` format (e.g., `4/11/2025`, `25/12/2025`)
- `tm/TIME`: Delivery time in 24-hour `HHmm` format (e.g., `1430` for 2:30 PM)
- `c/COST`: Delivery cost as decimal number (e.g., `28.50`, `420.00`)
- `r/REMARKS`: Special instructions or notes
- `t/TAG`: Optional label for categorization (alphanumeric only)

**Examples:**
```
add_delivery n/May Chen d/4/11/2025 tm/1430 c/28.50 r/2x laksa, leave at reception t/Personal
add_delivery n/Acme Pte Ltd d/4/11/2025 tm/1800 c/420.00 r/Company buffet t/Corporate
add_delivery n/John Doe d/15/3/2025 tm/0900 c/15.00 r/Nil
```

![Add Delivery](images/add_delivery.png)

**Important Notes:**
- **Client must exist first** - add the client before creating deliveries
- Client name matching is **case-sensitive** and must be exact
- Date format is flexible: `4/11/2025` or `04/11/2025` both work
- Time must be in 24-hour format (no AM/PM)
- Cost must be a valid number (can include decimals)
- New deliveries start as "not delivered" - use `mark` command to change status

---

### Editing a delivery : `edit_delivery`

Updates an existing delivery record by specifying the unique delivery ID. You can change any combination of delivery details.

**Format:**
```
edit_delivery DELIVERY_ID [n/NEW_NAME] [d/DATE tm/HHmm] [r/REMARKS] [c/COST] [t/TAG]
```

**How it works:**
- Finds the delivery specified by the delivery ID to edit
- Updates only the fields you specify
- If changing client name, the new client must already exist
- At least one field must be provided to update

**Parameter Details:**
- `DELIVERY_ID`: Unique delivery ID (must currently exist)
- `n/NEW_NAME`: Transfer delivery to a different existing client
- `d/DATE tm/TIME`: New delivery date and time (must provide both together)
- `r/REMARKS`: Update or replace special instructions
- `c/COST`: Change the delivery cost
- `t/TAG`: Change the delivery tag

**Examples:**
```
edit_delivery 3 n/May Chen tm/1515 r/Customer requested later pickup
edit_delivery 2 n/Acme Pte Ltd d/4/11/2025 tm/1830 c/450.00 t/Personal
edit_delivery 4 n/John Doe n/Jane Doe r/Address changed to office
```

![Edit Delivery](images/edit_delivery.png)

**Important Notes:**
- **Targets the specified delivery ID** supplied to edit 
- Client name must match exactly (case-sensitive)
- When changing schedule, you **must provide both date and time** together
- New client name (if changing) must already exist in your client list
- Use `list_delivery` to see delivery IDs if they currently exist
- The delivery's completion status (delivered/not delivered) is not affected

---

### Marking / Unmarking : `mark` / `unmark`

Updates the completion status of a delivery using its unique ID. Use `mark` when a delivery is completed, and `unmark` if you need to revert a delivery back to pending status.

**Formats:**
```
mark ID
unmark ID
```

**How it works:**
- `mark ID`: Sets delivery status to "Delivered: True"
- `unmark ID`: Sets delivery status to "Delivered: False"
- ID must be a valid delivery ID from your system
- Changes are immediately reflected in revenue calculations

**Examples:**
```
mark 1          # Marks delivery #1 as completed
unmark 3        # Marks delivery #3 as not delivered
mark 15         # Marks delivery #15 as completed
```

![Mark Delivery](images/mark_delivery.png)

**Finding Delivery IDs:**
- Use `list_delivery` to see all deliveries with their IDs
- IDs are unique numbers assigned automatically when deliveries are created
- Each delivery card displays its ID prominently

**Important Notes:**

- You can mark/unmark deliveries multiple times as needed
- The delivery ID must exist - you'll get an error for invalid IDs
- Useful for tracking which deliveries have been successfully completed

---

### Locating deliveries : `find_delivery`

Searches for deliveries based on client name, delivery date, or tags. You can combine multiple search criteria to narrow down results. The command uses **AND** logic - deliveries must match **all** provided criteria to be shown.

**Format:**
```
find_delivery [n/CLIENT_NAME] [d/DATE] [t/TAG]
```

**Search Behavior:**
- `n/CLIENT_NAME`: Searches for deliveries by client name match (partial match, case-insensitive)
- `d/DATE`: Finds deliveries on a specific date in `d/M/yyyy` format  
- `t/TAG`: Searches for deliveries with matching tags (partial match, case-insensitive)
- **At least one** search parameter must be provided
- Multiple criteria must **all** match (AND logic)

**Examples:**
```
find_delivery d/4/11/2025                   
find_delivery n/Acme Pte Ltd                       
```

![Find Delivery](images/find_delivery.png)

**Search Tips:**
- Client name searches are case-insensitive and allow partial matches
- Date searches show deliveries for that specific day only
- Tag searches are case-insensitive and allow partial matches
- Use `list_delivery` to see all deliveries if your search returns no results
- Combine criteria to find specific deliveries (e.g., urgent deliveries for a specific client)

---

### Deleting a delivery : `delete_delivery`

Permanently deletes a specific delivery record from FoodBook using its unique ID. This action cannot be undone except by using the `undo` command.

**Format:**
```
delete_delivery ID
```

**How it works:**
- Specify the exact delivery ID (a positive integer)
- The delivery and all its associated data will be permanently removed
- Revenue calculations will be updated to reflect the removal
- The client record remains unchanged (only the delivery is deleted)

**Examples:**
```
delete_delivery 1        # Deletes delivery with ID 1
delete_delivery 15       # Deletes delivery with ID 15
delete_delivery 203      # Deletes delivery with ID 203
```

![Delete Delivery](images/delete_delivery.png)

**Finding Delivery IDs:**
- Use `list_delivery` to see all deliveries with their unique IDs
- Each delivery card prominently displays its ID number
- IDs are assigned automatically when deliveries are created
- Use `find_delivery` to locate specific deliveries first

**⚠️ Important Warnings:**
- **This action is permanent** (except via `undo`)
- The delivery ID must exist - you'll get an error for invalid IDs
- Revenue totals will be recalculated after deletion
- The associated client remains in your system (only the delivery is removed)
- Double-check the ID before confirming deletion

**Before deleting:**
- Verify you have the correct delivery ID using `list_delivery`
- Consider if you need this data for business records or tax purposes
- Remember you can use `undo` immediately after if you make a mistake

---

## Revenue : `list_revenue`

Generates comprehensive revenue reports based on your delivery records. Only **completed (marked)** deliveries are included in revenue calculations. You can filter by date range, specific clients, tags, or delivery status to get detailed financial insights.

**Format:**
```
list_revenue [sd/DATE] [ed/DATE] [n/CLIENT_NAME] [t/TAG] [s/delivered|not_delivered]
```

**Parameter Details:**
- `sd/DATE`: Start date for the revenue period (inclusive)
- `ed/DATE`: End date for the revenue period (inclusive)  
- `n/CLIENT_NAME`: Show revenue from a specific client only
- `t/TAG`: Filter deliveries by tag (e.g., "Corporate", "Personal")
- `s/delivered`: Show only completed deliveries (default behavior)
- `s/not_delivered`: Show only pending deliveries (useful for pending revenue)

**Date Range Behavior:**
- No dates: Shows all-time revenue
- Only `sd/DATE`: Shows revenue for that single day
- Both dates: Shows revenue for the entire date range (inclusive)
- Dates use `d/M/yyyy` format (e.g., `1/10/2025`)

**Examples:**
```
list_revenue                                    # All-time total revenue
list_revenue sd/1/10/2025 ed/31/10/2025        # October 2025 revenue
list_revenue n/Acme Pte Ltd                    # Revenue from Acme Pte Ltd only
list_revenue sd/28/10/2025 s/delivered         # Completed deliveries on Oct 28
list_revenue t/Corporate sd/1/11/2025 ed/30/11/2025  # Corporate deliveries in November
list_revenue s/not_delivered                   # Pending revenue (undelivered orders)
```

![List Revenue](images/list_revenue.png)

**What you'll see:**
- Total revenue amount for the filtered period/criteria
- Number of deliveries included in the calculation
- Average delivery value
- Breakdown by completion status if relevant

**Important Notes:**
- **Only completed deliveries** contribute to revenue (use `mark` command first)
- Revenue calculations update automatically when deliveries are marked/unmarked
- Use date filters for monthly, weekly, or daily revenue reports
- Combine filters for detailed analysis (e.g., corporate clients in a specific month)

---

## Undo : `undo`

Reverses the most recent change made to your FoodBook data, restoring clients and deliveries to their previous state. This is your safety net for accidental edits, deletions, or unwanted changes.

**Format:** `undo`

**What can be undone:**
- Adding, editing, or deleting clients
- Adding, editing, or deleting deliveries  
- Marking/unmarking deliveries
- Clearing all data
- Any command that modified your data

**How it works:**
- You may undo a maximum of **5** times consecutively.
- Restores the complete previous state of your database
- Works immediately after the unwanted action
- Cannot undo multiple actions in sequence

**Examples:**
```
delete_client n/John Doe     # Accidentally deleted client
undo                        # Restores John Doe and all his deliveries

edit_delivery May Chen c/999.99    # Wrong cost entered
undo                              # Reverts to original cost

clear                      # Accidentally cleared all data  
undo                      # Restores everything
```

![Undo](images/undo.png)

**Important Limitations:**
- You may undo a maximum of **5** times consecutively.
- **Redo is not available** - once you undo, the "undone" action is lost forever
- Commands like `list_client` or `find_delivery` cannot be undone (they don't change data)

**Best Practices:**
- Use `undo` immediately after making a mistake
- Double-check important actions before confirming them
- Consider using search commands to verify data before making changes
- Remember that `undo` is your only way to recover from accidental deletions

---

## Clearing all entries : `clear`

Remove all data from FoodBook.

**Format:** `clear`

**What gets deleted:**
- **All client records** (names, contacts, addresses, tags)
- **All delivery records** (past, present, and future deliveries)
- **All revenue history** and financial data
- **Everything** - your database becomes completely empty

**Before using `clear`:**
- **Export or backup your data** if you need to keep records
- Consider if you really need to delete everything
- Remember that you can delete individual clients/deliveries instead
- Ensure you have business records elsewhere if needed for accounting/tax purposes

**Recovery options:**
- Use `undo` after `clear` to restore everything
- Restore from external backups if available

**When to use:**
- Starting fresh with a new business
- Testing the application with clean data
- Removing all test/demo data before production use
- **Never use accidentally** - double-check you typed the right command

---

## Exiting the program : `exit`

Safely closes FoodBook and terminates the application. All your data is automatically saved before the program exits, so you don't need to worry about losing any information.

**Format:** `exit`

**What happens when you exit:**
- All unsaved changes are automatically saved to disk
- The application window closes completely
- Your data remains safely stored for the next time you open FoodBook
- No confirmation prompt - the program exits immediately

**Alternative ways to exit:**
- Click the X button on the application window
- Use keyboard shortcut `Alt+F4` (Windows/Linux) or `Cmd+Q` (Mac)
- Type `exit` command in the command box

**Important Notes:**
- **Your data is always safe** - FoodBook auto-saves after every command
- No need to manually save before exiting
- The program will remember all your clients, deliveries, and settings for next time
- Exiting does not affect your data in any way - everything will be exactly as you left it when you restart

**Next startup:**
- All your clients and deliveries will be exactly as you left them
- The application will start with the same theme and window settings
- Your complete business data remains intact and accessible

---

## Saving the data

FoodBook auto-saves to disk after every successful command.

---

## Editing the data file

Data are stored as JSON at:
```
[JAR location]/data/foodbook.json
```

**Caution:** If you corrupt the JSON, FoodBook may start with an empty file on next launch. Keep backups.

---

## FAQ

**Keyboard-only usage?** Yes. The app is designed for fast CLI input; the GUI updates alongside.

**Tag colours?** `Personal` and `Corporate` have special colours; other tags use a default style.

**Invalid dates/times?** Commands are rejected with the expected format: `d/M/yyyy`, `HHmm`.

**Undo deletes?** Use `undo` to revert the last change. 

---

## Known issues

- **Multi-monitor:** If the app reopens off-screen, delete `preferences.json` and relaunch.
- **Help window:** If minimized, running `help` again won’t open a new one—restore the minimized window.

---

## Command Summary

| Action | Format |
|--------|--------|
| **List Clients** | `list_client` |
| **Add Client** | `add_client n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]` |
| **Edit Client** | `edit_client n/CURRENT_NAME [n/NEW_NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]` |
| **Delete Client** | `delete_client n/NAME` |
| **Find Client** | `find_client [n/NAME_KEYWORDS] [p/PHONE] [e/EMAIL]` |
| **List Deliveries** | `list_delivery` |
| **Add Delivery** | `add_delivery n/CLIENT_NAME d/DATE tm/TIME c/COST [r/REMARKS] [t/TAG]` |
| **Edit Delivery** | `edit_delivery CLIENT_NAME [n/NEW_NAME] [d/DATE tm/HHmm] [r/REMARKS] [c/COST]` |
| **Delete Delivery** | `delete_delivery ID` |
| **Mark / Unmark** | `mark ID` / `unmark ID` |
| **Find Delivery** | `find_delivery [n/CLIENT_NAME] [d/DATE] [t/TAG]` |
| **List Revenue** | `list_revenue [sd/DATE] [ed/DATE] [n/CLIENT_NAME] [t/tag] [s/delivered\|not_delivered]` |
| **Undo** | `undo` |
| **Help** | `help` |
| **Clear** | `clear` |
| **Exit** | `exit` |
