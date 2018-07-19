/*
 *
 *  * Copyright (c) 2018. Inäbnit Pascal
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package ch.hslu.swe.client;


import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RMIClient_Test {

    public RMIClient_Test() {
    }

    @Test
    @DisplayName("Test: Kontrolliert, dass die URL richtig zusammengesetzt sind.")
    public void testUrl() {
        String rmiServerIp = "192.168.1.1";
        int rmiPort = 1099;
        RMIClient client = new RMIClient(rmiServerIp, rmiPort);
        assertTrue(client.getUrl().equals("rmi://192.168.1.1:1099/RMI_SERVER_RO"));
    }
}