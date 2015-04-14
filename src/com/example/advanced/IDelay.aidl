package com.example.advanced;
import com.example.advanced.IRemoteCallback;

interface IDelay {
    void executeDelay();
    void registCallback(IRemoteCallback mCallback);
    void unRegistCallback(IRemoteCallback mCallback);
}