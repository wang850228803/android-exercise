/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/wanghui/backup/code/github/MyApp/app/src/main/aidl/com/example/advanced/IDelay.aidl
 */
package com.example.advanced;
public interface IDelay extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.example.advanced.IDelay
{
private static final java.lang.String DESCRIPTOR = "com.example.advanced.IDelay";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.example.advanced.IDelay interface,
 * generating a proxy if needed.
 */
public static com.example.advanced.IDelay asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.example.advanced.IDelay))) {
return ((com.example.advanced.IDelay)iin);
}
return new com.example.advanced.IDelay.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_executeDelay:
{
data.enforceInterface(DESCRIPTOR);
this.executeDelay();
reply.writeNoException();
return true;
}
case TRANSACTION_registCallback:
{
data.enforceInterface(DESCRIPTOR);
com.example.advanced.IRemoteCallback _arg0;
_arg0 = com.example.advanced.IRemoteCallback.Stub.asInterface(data.readStrongBinder());
this.registCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_unRegistCallback:
{
data.enforceInterface(DESCRIPTOR);
com.example.advanced.IRemoteCallback _arg0;
_arg0 = com.example.advanced.IRemoteCallback.Stub.asInterface(data.readStrongBinder());
this.unRegistCallback(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.example.advanced.IDelay
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void executeDelay() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_executeDelay, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void registCallback(com.example.advanced.IRemoteCallback mCallback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((mCallback!=null))?(mCallback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void unRegistCallback(com.example.advanced.IRemoteCallback mCallback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((mCallback!=null))?(mCallback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unRegistCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_executeDelay = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_registCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_unRegistCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public void executeDelay() throws android.os.RemoteException;
public void registCallback(com.example.advanced.IRemoteCallback mCallback) throws android.os.RemoteException;
public void unRegistCallback(com.example.advanced.IRemoteCallback mCallback) throws android.os.RemoteException;
}
