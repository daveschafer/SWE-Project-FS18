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
package ch.hslu.swe.server;

import ch.hslu.swe.helper.MoebelherstellerEnum;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface RMIInterface extends Remote {

    // Name des entfernten Objekts
    String RO_NAME = "RMI_SERVER_RO";

    /**
     *
     * @return @throws java.rmi.RemoteException - RMI Exception
     */
    int getMoebelhaeuser01(MoebelherstellerEnum moebelherstellerEnum) throws RemoteException;

    int getProductTypes02(MoebelherstellerEnum moebelherstellerEnum) throws RemoteException;
    //The Strings are actually JSONArrays and not Strings

    String getAverageOrderValuePerFurnitureShop03(MoebelherstellerEnum moebelherstellerEnum) throws RemoteException;

    String getOrderValueFromPeriod04(MoebelherstellerEnum moebelherstellerEnum) throws RemoteException;

    String getOrderValueFromPeriod04(MoebelherstellerEnum moebelherstellerEnum, Date Von, Date Bis) throws RemoteException;

    String getTop3FurnitureShops05(MoebelherstellerEnum moebelherstellerEnum, Date Von, Date Bis) throws RemoteException;

    String getTop3FurnitureShops05(MoebelherstellerEnum moebelherstellerEnum) throws RemoteException;

    double getAverageDeliveryTime06(MoebelherstellerEnum moebelherstellerEnum) throws RemoteException;

    String getTop5Products07(MoebelherstellerEnum moebelherstellerEnum, Date Von, Date Bis) throws RemoteException;

    String getTop5Products07(MoebelherstellerEnum moebelherstellerEnum) throws RemoteException;

    String getOrdersForAllWeeks08(MoebelherstellerEnum moebelherstellerEnum) throws RemoteException;

    String getOrderVolumeForAllWeeks09(MoebelherstellerEnum moebelherstellerEnum) throws RemoteException;

}
