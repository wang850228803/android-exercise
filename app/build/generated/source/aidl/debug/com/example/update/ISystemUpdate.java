/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/wanghui/backup/code/github/MyApp/app/src/main/aidl/com/example/update/ISystemUpdate.aidl
 */
package com.example.update;
public interface ISystemUpdate extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.example.update.ISystemUpdate
{
private static final java.lang.String DESCRIPTOR = "com.example.update.ISystemUpdate";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.example.update.ISystemUpdate interface,
 * generating a proxy if needed.
 */
public static com.example.update.ISystemUpdate asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.example.update.ISystemUpdate))) {
return ((com.example.update.ISystemUpdate)iin);
}
return new com.example.update.ISystemUpdate.Stub.Proxy(obj);
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
case TRANSACTION_addListener:
{
data.enforceInterface(DESCRIPTOR);
com.example.update.ISystemUpdateListener _arg0;
_arg0 = com.example.update.ISystemUpdateListener.Stub.asInterface(data.readStrongBinder());
int _result = this.addListener(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_removeListener:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.removeListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_startDownload:
{
data.enforceInterface(DESCRIPTOR);
this.startDownload();
reply.writeNoException();
return true;
}
case TRANSACTION_cancelDownload:
{
data.enforceInterface(DESCRIPTOR);
this.cancelDownload();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.example.update.ISystemUpdate
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
@Override public int addListener(com.example.update.ISystemUpdateListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_addListener, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void removeListener(int index) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(index);
mRemote.transact(Stub.TRANSACTION_removeListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void startDownload() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startDownload, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void cancelDownload() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_cancelDownload, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_addListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_removeListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_startDownload = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_cancelDownload = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public int addListener(com.example.update.ISystemUpdateListener listener) throws android.os.RemoteException;
public void removeListener(int index) throws android.os.RemoteException;
public void startDownload() throws android.os.RemoteException;
public void cancelDownload() throws android.os.RemoteException;
}
