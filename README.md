# Welly

A wellbeing tracker for Android — meals, macros, and hydration, built offline-first with Kotlin and Jetpack Compose.

Welly lets you log meals and drinks, see how they add up against daily calorie/macro/water targets, and estimate nutrition through an external AI assistant instead of a paid API — you copy a generated prompt, run it wherever you already have an LLM, and paste the JSON answer back in.

## Demo

<table>
<tr>
<th>Add a meal</th>
<th>Add a drink</th>
</tr>
<tr>
<td>

https://github.com/user-attachments/assets/27112304-fccd-4c97-91db-59c149986f7b

[▶ Watch the flow](docs/media/add-drink-flow.mp4)
 
</td>
<td>
  
https://github.com/user-attachments/assets/386f7db6-530c-427b-b9ff-15466b10883a

[▶ Watch the flow](docs/media/add-meal-flow.mp4)
 
</td>
</tr>
</table>

## What's working today

- **Today dashboard** — day-by-day navigation, a calorie card and a protein/carbs/fat/fiber grid each showing how today's totals sit against a target range (with a below/within/above indicator), a hydration card with a progress bar, and a chronological list of the day's meals and drinks.
- **Add meal** — meal type, backdated date & time, free-text description, and the clipboard-based AI macro flow described below.
- **Add drink** — caloric/non-caloric toggle, a quick amount stepper (with editable input and preset chips), and the same AI macro flow for caloric drinks (non-caloric ones skip it — only hydration counts).
- **Local persistence** — meals and drinks are stored in Room and read back reactively with Flow; nothing needs a network connection.
- **Localization** — UI strings ship in English and Polish (`values` / `values-pl`), following the device locale.
- **Custom Material 3 theme** — a dedicated sage/green palette instead of the default dynamic/purple theme, with light and dark variants.

### The AI macro flow

Rather than wiring up a paid LLM API this early, macro estimation is a deliberate two-step handoff:

1. **Copy prompt** — Welly builds a prompt from a fixed template plus your meal/drink description and puts it on the clipboard.
2. **Paste response** — you run that prompt in whatever AI assistant you already use, then paste the JSON it returns back into Welly, which parses and validates it before showing a preview.

This keeps the domain logic (prompt building, JSON parsing) real and testable without taking on API costs or key management yet — an actual LLM call is a drop-in replacement for the clipboard step later.

## In progress / planned

- **Profile & onboarding** — daily calorie/macro/water targets are currently computed from a hardcoded placeholder profile (Mifflin–St Jeor BMR → TDEE → macro split); a real profile screen will replace it and let targets reflect the signed-in user.
- **Stats screen** — currently a stub; weekly/monthly macro and hydration summaries are next.
- **Workout tracking** — logging training sessions alongside meals and drinks, for a fuller picture of the day than nutrition alone.
- **Rewards shop** — a points system that rewards staying within your daily targets, redeemable in a shop.
- **Remote sync** — the repository layer is already split from Room behind an interface so a remote data source can be added later without touching the UI or domain logic.

## Tech stack

| Layer | Choice |
|---|---|
| UI | Jetpack Compose, Material 3 |
| Architecture | Clean Architecture (presentation / domain / data) |
| DI | Hilt |
| Local storage | Room |
| Navigation | Navigation Compose (type-safe routes via `kotlinx.serialization`) |
| Async | Kotlin Coroutines + Flow |
| Static analysis | ktlint, detekt, Android Lint |

## Getting started

```bash
git clone https://github.com/jula1717/Welly
cd Welly
./gradlew installDebug   # build and install the debug APK on a connected device/emulator
```

Requires JDK 17+ and the Android SDK (compileSdk 37, minSdk 30).
