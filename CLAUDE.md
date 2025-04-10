# MyclinicScala Development Guide

## Build Commands
- Compile: `sbt compile`
- Run tests: `sbt test`
- Run single test: `sbt "testOnly *SpecificTestName"`
- Run application: `sbt run`
- Clean build: `sbt clean`
- Compile specific module: `sbt "project moduleName" compile`

## Code Style Guidelines
- **Formatting**: 2-space indentation, max line length ~100 chars
- **Naming**: PascalCase for classes, camelCase for methods/variables
- **Imports**: Standard library first, external libs next, project imports last
- **Type System**: Prefer immutable data structures, use case classes for models
- **Error Handling**: Use Option for nullable values, Either for operations that might fail
- **Testing**: Write tests using ScalaTest in AnyFunSuite style
- **Architecture**: Follow modular design with clear separation of concerns
- **Documentation**: Self-document through clear naming and function signatures

## Common Libraries
- cats/cats-effect for FP, doobie for DB, http4s for HTTP, circe for JSON

## Required Setup
- JDK: 11+ recommended
- SBT: 1.9.0+ recommended