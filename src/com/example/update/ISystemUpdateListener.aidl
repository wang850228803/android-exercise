package com.example.update;

interface ISystemUpdateListener {

	void updateUI(int status);
	void updateProgressBar(int progress);
}