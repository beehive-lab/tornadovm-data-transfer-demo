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
package demo.scenarios;

import demo.kernels.SaxpyKernel;
import demo.kernels.MaxKernel;
import demo.utils.DataGenerator;
import demo.utils.Timer;
import demo.utils.Validator;
import uk.ac.manchester.tornado.api.TaskGraph;
import uk.ac.manchester.tornado.api.TornadoExecutionPlan;
import uk.ac.manchester.tornado.api.enums.DataTransferMode;
import uk.ac.manchester.tornado.api.exceptions.TornadoExecutionPlanException;

import java.util.Arrays;

public class Scenario5_ChainedPipeline_MultipleTaskGraphs {

    public static void run(int size) throws TornadoExecutionPlanException {

        float[] x = DataGenerator.randomArray(size);
        float[] xJava = Arrays.copyOf(x, x.length);
        float[] y = DataGenerator.randomArray(size);
        float[] yJava = Arrays.copyOf(y, y.length);
        float[] maxResult = new float[1];
        float[] maxResultJava = new float[1];
        float alpha = 2.0f;

        TaskGraph tg1 = new TaskGraph("saxpy-s5.0")
                .transferToDevice(DataTransferMode.FIRST_EXECUTION, x, y)
                // Task 1: SAXPY
                .task("saxpy", SaxpyKernel::saxpy, x, y, alpha)
                .persistOnDevice(y);
//                .transferToHost(DataTransferMode.EVERY_EXECUTION, y);

        TaskGraph tg2 = new TaskGraph("saxpy-s5.1")
//                .transferToDevice(DataTransferMode.EVERY_EXECUTION, y)
                .consumeFromDevice(y)
                // Task 2: Max computation (consumes SAXPY output)
                .task("max", MaxKernel::computeMax, y, maxResult)
                .transferToHost(DataTransferMode.EVERY_EXECUTION, maxResult);

        try (TornadoExecutionPlan executionPlan = new TornadoExecutionPlan(tg1.snapshot(), tg2.snapshot())) {
            executionPlan.withBatch("64MB");
            Timer t = new Timer();
            t.start();
            executionPlan.execute();
            double totalMs = t.stopMillis();

            // Run the same computation in Java
            SaxpyKernel.saxpy(xJava, yJava, alpha);
            MaxKernel.computeMax(yJava, maxResultJava);

            // Validate results
            boolean status = Validator.saxpy(yJava, y);
            status &= Validator.maxReduce(maxResultJava, maxResult);
            System.out.printf("[Scenario 5 - Chained Tasks in Multiple TaskGraphs] Size=%d, Max=%.4f, Total=%.2f ms, Validation=%s%n", size, maxResult[0], totalMs, Validator.isValid(status));
        }
    }
}
