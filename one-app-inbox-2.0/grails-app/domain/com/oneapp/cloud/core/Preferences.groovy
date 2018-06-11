package com.oneapp.cloud.core

import java.io.Serializable;

class Preferences implements Serializable{
	
	long feedUpdateFrequency
	int daysToRetrievePosts
	
    static constraints = {
    }
}
