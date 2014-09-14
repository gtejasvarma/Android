package com.example.tejasscheduler.ui.activity;

import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;

import com.example.tejasscheduler.R;

public  class MyPreferenceFragment extends PreferenceFragment
  {
	
public MyPreferenceFragment(){
	  
}
  @Override
  public void onCreate( Bundle savedInstanceState)
  {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.settings);
  }

}