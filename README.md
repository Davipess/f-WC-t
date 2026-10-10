# fWCt (FCT WC Finder)

Campus restroom discovery and crowd-sourced management system for NOVA SST.

## Status

Prototype, in progress. Built as a Java console prototype to test the domain model and business rules before developing a mobile app.

## What It Does

- Finds the best available, highest-rated restroom in a building with optional floor and gender filters.
- Community consensus alerts: a reported issue (cleaning or maintenance) starts as pending and requires two independent confirmations before locking the facility.
- Administrator controls: facilities can be marked resolved and reopened only by an admin.
- Anti-spam review system: users can submit at most one review per restroom during each weekly contest cycle.
- Detailed facility cards displaying operational status, active alerts, and review history.

## How I Built It

I directed AI-assisted development of this project. The architecture was directly inspired by my university Object-Oriented Programming course project (YouVideo). I designed the layered structure with five dedicated managers behind a central facade (FWCt), an abstract bathroom hierarchy (AbstractBathroomClass), and domain-specific checked exceptions. I guided AI tools to implement the manager classes and console handlers, then reviewed the code to enforce encapsulation, ensure domain classes do not perform console I/O, and verify that the bubble-sort ranking correctly sorts descending by rating.

## Tech Stack

- Java 21
- JUnit 5
- Pure Java collections (no external frameworks or libraries beyond JUnit)

## How to Run

1. Compile the project (cross-platform):

On Linux / macOS:
```bash
javac -d bin -cp "lib/*" src/**/*.java src/**/**/*.java tests/*.java
```

On Windows (PowerShell or Command Prompt):
```powershell
javac -d bin -cp "lib/*;src;tests" (Get-ChildItem -Recurse -Filter "*.java" src,tests).FullName
```

2. Run the interactive console application:
```bash
java -cp bin Main
```

3. Run the automated demonstration script:
```bash
# Linux / macOS:
java -cp bin Main < tests/sample_run_input.txt

# Windows (PowerShell):
Get-Content tests/sample_run_input.txt | java -cp bin Main
```

## Tests

The project includes 14 JUnit 5 tests covering user authentication, building management, bathroom state transitions, and facade operations.

To run tests without Maven or Gradle:
```bash
# Linux / macOS:
java -cp "bin:lib/*" TestRunner

# Windows:
java -cp "bin;lib/*" TestRunner
```

Expected result: 15 passed, 0 failed.

## Known Limitations and Next Steps

- Console interface: currently runs only as a text terminal application.
- In-memory persistence: state resets when the application process terminates.
- Next steps include building a REST API backend with persistent storage and developing a React Native mobile client with campus maps.

## License

MIT License.
