# TsayYongBot — User Guide

> “Your mind is for having ideas, not holding them.” — David Allen

TsayYongBot is a **personal assistant chatbot** that helps you track tasks via a simple command interface. It supports **ToDos, Deadlines, Events**, searching, marking/unmarking, deleting, and a handy **snooze** command to reschedule items. Data is **auto-saved** to your disk.

---

![screenshot](Ui.png)

## Table of Contents

* [Quick Start](#quick-start)
* [How to Run](#how-to-run)

  * [Run with Gradle](#run-with-gradle)
  * [Run from JAR](#run-from-jar)
* [Using the App](#using-the-app)

  * [Command Format](#command-format)
  * [Features](#features)
* [Date & Time Formats](#date--time-formats)
* [Storage](#storage)
* [GUI Notes](#gui-notes)
* [Testing](#testing)
* [Troubleshooting](#troubleshooting)
* [Command Summary](#command-summary)

---

## Quick Start

1. Ensure you have **Java 17** installed (`java -version`).
2. Clone your fork of the repo and open it in your IDE/terminal.
3. Run the app (see [How to Run](#how-to-run)).
4. Type commands into the input box (GUI) or stdin (CLI) — see [Features](#features).

---

## How to Run

### Run with Gradle

```bash
# From project root
./gradlew run          # macOS/Linux
# or
.\gradlew run          # Windows PowerShell / CMD
```

This launches the **GUI** (JavaFX). The app window is titled *TsayYongBot*.

### Run from JAR

The FAT Jar will also be available on the [GitHub Releases page](https://github.com/nty-dev/ip/releases). Run it in an empty folder:

```bash
java -jar TsayYongBot.jar
```

---

## Using the App

### Command Format

* Words in **angle brackets** are parameters you supply, e.g., `<description>`.
* **Dates/times** accept several formats; see [Date & Time Formats](#date--time-formats).
* Commands are **case-insensitive** for keywords (e.g., `TODO` ≈ `todo`).

### Features

#### 1) Greet & Exit

* **Exit:** `bye`

  * Closes the app politely.

#### 2) List Tasks

* **List all:** `list`

  * Shows your tasks with indices.

#### 3) Add Tasks

* **ToDo:** `todo <description>`

  * Example: `todo read book`
* **Deadline:** `deadline <description> /by <when>`

  * Example: `deadline return book /by 2019-12-02`
* **Event:** `event <description> /from <start> /to <end>`

  * Example: `event project meeting /from 2019-10-01 14:00 /to 2019-10-01 16:00`

**Output example:**

```
____________________________________________________________
 Got it. I've added this task:
  [D][ ] return book (by: Dec 2 2019)
 Now you have 6 tasks in the list.
____________________________________________________________
```

#### 4) Mark / Unmark

* **Mark done:** `mark <task-number>`
* **Mark not done:** `unmark <task-number>`

#### 5) Delete

* **Delete:** `delete <task-number>`

#### 6) Find

* **Search by keyword:** `find <keyword>`

  * Example: `find book`

#### 7) Snooze (Reschedule)

> *Extension: B‑Snooze*

Reschedule **Deadlines** or **Events** without re-adding them.

* **Deadline:** `snooze <task-number> /by <when>`

  * Example: `snooze 3 /by 2025-09-15`
* **Event:** `snooze <task-number> /from <start> /to <end>`

  * Example: `snooze 4 /from 2025-09-20 09:00 /to 2025-09-20 11:00`

If the new date/time is recognized, the task displays a **prettified** date (e.g., `Sep 15 2025` or `Sep 20 2025, 11:00 AM`).

#### 8) Error Handling

The bot gives clear messages for invalid inputs, e.g., missing description, out-of-range indices, or wrong formats. Try `help` (if enabled in your build) or refer to this guide.

---

## Date & Time Formats

TsayYongBot accepts multiple common input formats and prints **friendly** dates.

**Accepted inputs (examples):**

* **Date only**

  * `2019-12-02` (ISO)  → **Dec 2 2019**
  * `15/09/2025` or `1/9/2025` → **Sep 15 2025**, **Sep 1 2025**
  * `Sep 12 2025`, `September 12 2025`
* **Date + time**

  * `2019-12-02 1800` or `2019-12-02 18:00` → **Dec 2 2019, 6:00 PM**
  * `1/10/2019 14:00` → **Oct 1 2019, 2:00 PM**
  * `Sep 12 2025 09:30` → **Sep 12 2025, 9:30 AM**

> Note: Abbreviate months as `Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec` (not `Sept`). Unrecognized inputs are kept as‑is.

---

## Storage

* File: `data/tsayyongbot.jsonl`
* Format: **JSON Lines** (one JSON object per line). Example:

```json
{"type":"T","done":true,"desc_b64":"cmVhZCBib29r"}
{"type":"D","done":false,"desc_b64":"cmV0dXJuIGJvb2s=","by_b64":"RGVjIDIgMjAxOSw gNiowMCBQTQ=="}
```

> The app **auto-saves** after each change and **auto-loads** at startup. It also creates the `data/` folder on first run if missing.

---

## GUI Notes

TsayYongBot comes with a JavaFX GUI:

* **Asymmetric chat bubbles** distinguish user vs bot.
* **Errors are highlighted** for visibility.
* **Resizable** window; contents adapt to size.
* Optional visuals (if added in your build):

  * Background image, app icon, and avatars for user/bot.
  * Place assets under `src/main/resources/images/` and link via CSS/FXML.

> Tip: If the GUI fails to start, verify you’re on **Java 17** and your Gradle JavaFX dependencies match.

---

## Testing

### 1) Text‑UI Testing (for CLI)

A semi-automated test harness is provided in `text-ui-test/`:

```bat
text-ui-test\runtest.bat
```

It compiles sources, runs the app with `input.txt`, and compares the output with `EXPECTED.TXT`.

### 2) JUnit Tests

Run unit tests with:

```bash
./gradlew test
```

---

## Troubleshooting

* **Gradle error: “Unsupported class file major version 67”**

  * You’re using a newer JDK. Use **JDK 17**, or set `org.gradle.java.home` to your JDK‑17 path.
* **Dates not prettified**

  * Use one of the supported input formats; otherwise the original text is shown.
* **JavaFX launch issues**

  * Ensure internet access for dependency download on first build, then retry `./gradlew run`.

---

## Command Summary

| Action       | Command                                        |
| ------------ | ---------------------------------------------- |
| Exit         | `bye`                                          |
| List         | `list`                                         |
| Add ToDo     | `todo <description>`                           |
| Add Deadline | `deadline <description> /by <when>`            |
| Add Event    | `event <description> /from <start> /to <end>`  |
| Mark         | `mark <task-number>`                           |
| Unmark       | `unmark <task-number>`                         |
| Delete       | `delete <task-number>`                         |
| Find         | `find <keyword>`                               |
| Snooze (DL)  | `snooze <task-number> /by <when>`              |
| Snooze (EV)  | `snooze <task-number> /from <start> /to <end>` |

---

**Enjoy TsayYongBot!** 😊 If you find a bug or have a feature idea, please open an issue.
