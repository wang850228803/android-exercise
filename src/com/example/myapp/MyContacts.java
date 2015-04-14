package com.example.myapp;

import android.net.Uri;
import android.provider.BaseColumns;

public final class MyContacts {
    public static final String AUTHORITY="com.myapp.contactscontentprovider";
    
    private MyContacts () {}
    
    public static final class ContactColumns implements BaseColumns{
        
        private ContactColumns () {}
        
        public static final Uri CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/contacts");
        
        public static final String NAME="name";
        
        public static final String NUM="number";
        
        public static final String DEFAULT_ORDER="name DESC";
    } 
}
