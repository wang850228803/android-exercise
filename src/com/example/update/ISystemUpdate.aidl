package com.example.update;
import com.example.update.ISystemUpdateListener;

interface ISystemUpdate {
	int addListener(ISystemUpdateListener listener);
	void removeListener(int index);
}