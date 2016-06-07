/**
 * ColltnAndPmtService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.zlebank.zplatform.trade.bosspay.service;

public interface ColltnAndPmtService extends java.rmi.Remote {
    public java.lang.String findRealTmPmt(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.String findRealTmColltn(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.String btchPmt(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.String btchColltn(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.String realTmPmt(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.String findBtchPmt(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.String findBtchColltn(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.String realTmColltn(java.lang.String arg0) throws java.rmi.RemoteException;
}
