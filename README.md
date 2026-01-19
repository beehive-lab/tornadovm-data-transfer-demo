# Optimizing I/O Data Transfer with TornadoVM - Demo

This project demonstrates data transfer patterns using TornadoVM:

1. FIRST_EXECUTION vs EVERY_EXECUTION (for input data)
2. EVERY_EXECUTION vs UNDER_DEMAND (for output data)
3. UNDER_DEMAND (for a partial output using DataRange)
4. Chain multiple tasks to return the final result

## Requirements
- TornadoVM installed and sourced
- Java 21+
- GPU backend (OpenCL / PTX / SPIR-V)

## Build & Run
The main class requires an integer to define which scenarios to run.
Passing `1234` is treated as a special value and triggers execution of all four scenarios.
Passing any other number (such as `3`) limits execution to the indexed scenario.

To build:
```bash
mvn clean package
```

To run all scenarios:
```bash
tornado --jvm="-Xmx24g" -cp target/tornadovm-data-transfer-demo-1.0-SNAPSHOT.jar demo.Main 1234 
```

or 

```bash
java @$TORNADOVM_HOME/tornado-argfile -Xmx24g -cp target/tornadovm-data-transfer-demo-1.0-SNAPSHOT.jar demo.Main 1234
```


## Acknowledgments

This work is partially funded by the following EU & UKRI grants (most recent first):

- EU Horizon Europe & UKRI [AERO 101092850](https://aero-project.eu/).
- EU Horizon Europe & UKRI [P2CODE 101093069](https://p2code-project.eu/).