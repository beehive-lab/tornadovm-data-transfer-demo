/*
 * Copyright 2026, APT Group, Department of Computer Science,
 * The University of Manchester.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package demo;

import demo.scenarios.Scenario1_FitsInGPU;
import demo.scenarios.Scenario2_FitsInGPU_UnderDemand;
import demo.scenarios.Scenario3_FitsInGPU_UnderDemand_DataRange;
import demo.scenarios.Scenario4_ChainedPipeline;
import uk.ac.manchester.tornado.api.exceptions.TornadoExecutionPlanException;

public class Main {

    static void main(String[] args) throws TornadoExecutionPlanException {

        int executed_scenario = Integer.parseInt(args[0]);
        int small = 100_000_000;     // fits GPU
        int large = 1_000_000_000;    // does not fit GPU, using batching

        // Run benchmarks
        switch (executed_scenario) {
            case 1 -> Scenario1_FitsInGPU.run(small);                       // with full transfer
            case 2 -> Scenario2_FitsInGPU_UnderDemand.run(small);           // with UNDER_DEMAND transfer
            case 3 -> Scenario3_FitsInGPU_UnderDemand_DataRange.run(small); // with UNDER_DEMAND transfer & Data Range
            case 4 -> Scenario4_ChainedPipeline.run(large);                 // Chained TaskGraph
            case 12 -> {
                Scenario1_FitsInGPU.run(small);                             // with full transfer
                Scenario2_FitsInGPU_UnderDemand.run(small);                 // with UNDER_DEMAND transfer
            }
            case 23 -> {
                Scenario2_FitsInGPU_UnderDemand.run(small);                 // with UNDER_DEMAND transfer
                Scenario3_FitsInGPU_UnderDemand_DataRange.run(small);       // with UNDER_DEMAND transfer & Data Range
            }
            case 123 -> {
                Scenario1_FitsInGPU.run(small);                             // with full transfer
                Scenario2_FitsInGPU_UnderDemand.run(small);                 // with UNDER_DEMAND transfer
                Scenario3_FitsInGPU_UnderDemand_DataRange.run(small);       // with UNDER_DEMAND transfer & Data Range
            } case 1234 -> {
                Scenario1_FitsInGPU.run(small);                             // with full transfer
                Scenario2_FitsInGPU_UnderDemand.run(small);                 // with UNDER_DEMAND transfer
                Scenario3_FitsInGPU_UnderDemand_DataRange.run(small);       // with UNDER_DEMAND transfer & Data Range
                Scenario4_ChainedPipeline.run(large);                       // Chained TaskGraph
            }
        }
    }
}
