# Tesla domain model

A Java 21 project that implements the [Tesla PlantUML diagram](docs/tesla-refined.puml) with Jakarta Persistence, Hibernate 6, and an in-memory H2 database. The demo writes and reloads an owner, vehicle, service appointment, and charging session, along with their related objects.

## Run in IntelliJ IDEA

1. Open this folder or its `pom.xml` as a Maven project.
2. Set the project SDK and the Maven runner JDK to **JDK 21**.
3. Run `school.tesla.Demo` or run the Maven commands below from the project folder.

The Maven Wrapper downloads Maven and the dependencies on the first run. For terminal commands, point `JAVA_HOME` to your JDK 21 installation. The database is created fresh each time the demo runs, and its contents are discarded when the session factory closes.

| Task | Windows PowerShell | macOS / Linux |
| --- | --- | --- |
| Build and run all tests | `.\mvnw.cmd clean verify` | `./mvnw clean verify` |
| Run the sample | `.\mvnw.cmd compile exec:java` | `./mvnw compile exec:java` |
| Show formatted SQL and bind values | `.\mvnw.cmd '-Dtesla.sql=true' '-Dtesla.bind=trace' compile exec:java` | `./mvnw -Dtesla.sql=true -Dtesla.bind=trace compile exec:java` |

If your unzip tool drops the Unix executable bit, run `bash mvnw clean verify`.

## How the code maps to the UML

| UML area | Java and JPA mapping |
| --- | --- |
| Identity | Abstract `User` with joined-table inheritance for `Owner` and `Technician`. Certifications use a set of converted `TeslaModel` values. |
| Vehicles | `Vehicle` references one `Owner` and embeds one `BatteryPack`. |
| Service | An appointment references one vehicle and one service center, with an optional technician. Its ordered list of `ServiceTask` values uses `@ElementCollection` and `@OrderColumn`. |
| Charging | A session references one vehicle and one station. Each station owns an ordered list of `ChargingStall` values. An open session can have a null end time and total cost. |
| Shared value objects | `Address`, `GeoCoordinate`, `Money`, and the UML's named scalar types are immutable embeddable records. |

Constructors validate lengths, formats, and numeric ranges. They also require at least one task per appointment and one stall per station. Required columns and relationships are backed by database constraints. The software version format is `yyyy.ww.x` with week `01`–`53` and a numeric revision. Monetary amounts use `NUMERIC(19,4)` and reject values that would require rounding.

## How the code maps to the class notes

- The Tesla enums keep the UML constants, including `MODEL_Lpromax`. Each converter maps its enum to explicit database codes. For example, `MODEL_Lpromax` maps to `LP`, and `CONFIRMED` maps to `CONF`. The switches in the converters define the full mapping.
- Entity and embeddable fields use `@Convert`. Each converter handles `null` before the switch and throws a shared `DataQualityException` for an unknown database code.
- The `lesson` package contains the notes' `Photo` and `Orientation` example. Its own persistence unit stores `PORTRAIT`, `LANDSCAPE`, and `SQUARE` as `P`, `L`, and `S`.
- JUnit checks every code in both directions. The H2 tests also inspect stored values with SQL, reload the object graph, check list order and optional values, and exercise the exception after writing an invalid code through SQL.
- For the demo and Maven tests, `tesla.sql` enables Hibernate's formatted SQL, and `tesla.bind=trace` enables `org.hibernate.orm.jdbc.bind` logging.

## Share with your group on GitHub

1. Create an empty repository under your GitHub account or your class organization. Use the visibility required by your class.
2. From the project folder, run these commands if you extracted the ZIP and are starting a new local Git repository.

   ```sh
   git init -b main
   git add .
   git add --chmod=+x mvnw
   git commit -m "Add Tesla Java persistence project"
   ```

3. From the local repository, add your new GitHub remote and push. Replace `OWNER` and `REPO` with the values in your repository URL.

   ```sh
   git remote add origin https://github.com/rioslavu/TesLAR--RiosLavu-LeaSchaffner-POS-Projerct
   git push -u origin main
   ```

4. For a personal repository, open **Settings → Collaborators → Add people** and invite your teammates by GitHub username. They can clone the repository after accepting. For an organization repository, use its repository access settings.

Each teammate can create a branch with `git switch -c feature/your-task`, run the tests before pushing, and open a pull request into `main`.

[GitHub instructions for pushing local code](https://docs.github.com/en/migrations/importing-source-code/using-the-command-line-to-import-source-code/adding-locally-hosted-code-to-github) · [GitHub instructions for inviting collaborators](https://docs.github.com/en/repositories/managing-your-repositorys-settings-and-features/repository-access-and-collaboration/inviting-collaborators-to-a-personal-repository)
