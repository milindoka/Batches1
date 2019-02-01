package in.refort.batches;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PlanetsActivity extends Activity {
  
 ;
private ListView mainListView ;
  private Planet[] planets ;
  private ArrayAdapter<Planet> listAdapter ;
  private boolean AlreadyPicked=false;
  ArrayList<String> Roll = new ArrayList<String>();
  ArrayList<String> tempRoll = new ArrayList<String>();
  ArrayList<Boolean> checkmark = new ArrayList<Boolean>();
  ArrayList<Planet> planetList = new ArrayList<Planet>();
  
  boolean modified=false,NewNow=false,selectall=false,end=false,OpenNow=false;
  String  BatchNo="01",Date="",BatchTime="",School="SIWS College",Index="J-31.04.005",
		  Strim="Science", Standard="HSC",Subject="Mathematics",SubjectCode="40",Type="Practical",
		  Email1="",Email2="",BatchCreator="MO",BatchSession="";
   
          
  static int PickCounter=0;
  String ProgramName="Batches 1.4";
  String FileName="Untitled";
  
  String froll="M058151",lroll="M058200",FileNameWithPath="",tempstr;
  int maxstrength=5000; ////maximum strength
  public void showtop(String tempstr)
  {	
///////////////////  SHIFTED TO TOP
  	
          	Toast toast= Toast.makeText(getBaseContext(), 
  			tempstr, Toast.LENGTH_SHORT);                                      
  			toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0); ///top page,reqd import
  			//toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
  	toast.show();
 
////////////////////////////
 /* 	
  	Toast toast = Toast.makeText(this, tempstr, Toast.LENGTH_SHORT);
  	TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
      v.setTextColor(Color.RED);
      v.setBackgroundColor(Color.YELLOW);
  	toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0); ///top page,reqd import
      v.setTextSize(22);
  	toast.show();
 */
 /////////////////////////////////////////////////////////////////////
  	
  	/*
  	 Toast toast = Toast.makeText(context, R.string.yummyToast, Toast.LENGTH_SHORT);
  	    LinearLayout toastLayout = (LinearLayout) toast.getView();
  	    TextView toastTV = (TextView) toastLayout.getChildAt(0);
  	    toastTV.setTextSize(30);
  	    toast.show();
  	*/
  
  }
  
  public void show(int tempnum)
  {
  	Toast.makeText(getBaseContext(),String.valueOf(tempnum),Toast.LENGTH_SHORT).show();
  }
  
  public void show(String tempstring)
  {
  	Toast.makeText(getBaseContext(),tempstring,Toast.LENGTH_SHORT).show();
  }
  
  
  @Override
  public void onBackPressed() 
  {
      if(modified) Bye();
      else finish();
      return;
  }
  
  
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    // Find the ListView resource. 
    mainListView = (ListView) findViewById( R.id.mainListView );
    
    // When item is tapped, toggle checked properties of CheckBox and Planet.
    mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick( AdapterView<?> parent, View item, 
                               int position, long id)
      { 
    	  
    	  if(AlreadyPicked)  ///disable clicks if already picked and leave without updating counter
          {  return; }
      
        Planet planet = listAdapter.getItem( position );
        planet.toggleChecked();
        PlanetViewHolder viewHolder = (PlanetViewHolder) item.getTag();
        viewHolder.getCheckBox().setChecked( planet.isChecked() );
        UpdateTitle();
      }
    });

    
    // Set our custom array adapter as the ListView's adapter.
    listAdapter = new PlanetArrayAdapter(this, planetList);
    mainListView.setAdapter( listAdapter );      
    
    FillList(froll,lroll);

    UpdateTitle();
    
//??
    
////load preferences       
    SharedPreferences settings = getSharedPreferences("BATCH-PREF", 0);
    School=settings.getString("School",School);
    Index=settings.getString("Index", Index);
    Strim=settings.getString("Strim", Strim);
    Standard=settings.getString("Standard", Standard);
    Subject=settings.getString("Subject", Subject);
    SubjectCode=settings.getString("SubjectCode", SubjectCode);
    Type=settings.getString("Type", Type);
    Email1=settings.getString("Email1", Email1);
    Email2=settings.getString("Email2", Email2);
    BatchCreator=settings.getString("BatchCreator", BatchCreator);
  
    
    ////// End of Initial Update
    
    
   
    
    
    
    final Button buttonNew = (Button) findViewById(R.id.btnNew);
	buttonNew.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v)
		{  if(modified)
				WarnBeforeNew();
		else
			GetNewRoll();
		   
		}
		});

	
	final Button buttonSave = (Button) findViewById(R.id.btnSave);
	buttonSave.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v)
		{   if(PickCounter==0) {show("No Selction To Save"); return;}
			SaveBatchList();
		}
		});

	final Button buttonSend = (Button) findViewById(R.id.btnSend);
	buttonSend.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v)
		{  SendBatch();
		}
		});
	

	final Button buttonLoad = (Button) findViewById(R.id.btnLoad);
	buttonLoad.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v)
		{ //LoadBatch()
			if(modified)
				WarnBeforeOpen();
			else
			OpenFileDialog(); //// It will call go to load batch
		}
		});

	
	
	
	
	final Button buttonSet = (Button) findViewById(R.id.btnSet);
	buttonSet.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v)
		{  GetSetDlg();
		}
		});

	
	
	
	final Button buttonHeader = (Button) findViewById(R.id.btnHeader);
	buttonHeader.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v)
		{ GetHeaderDlg();
        }
		});

	
	final Button buttonHelp = (Button) findViewById(R.id.btnHelp);
	buttonHelp.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v)
		{ showHelp();
        }
		});

	
	  
	
	
	final Button buttonSelectAll = (Button) findViewById(R.id.btnSelectAll);
	buttonSelectAll.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v)
		{ selectall=!selectall;
		
			String temproll;
			
	       planetList.removeAll(planetList);
	       checkmark.removeAll(checkmark);
		    for(int i=0;i<Roll.size();i++)
		      { 
			    temproll=Roll.get(i);
		         Planet tempPlanet=new Planet(temproll);
		        tempPlanet.setChecked(selectall);
		         planetList.add(tempPlanet);
		        // Boolean tempbool=new Boolean();
		         checkmark.add(selectall);
		         
		      }
		    
		    UpdateTitle();
		    ((BaseAdapter) mainListView.getAdapter()).notifyDataSetChanged();
	 	    AlreadyPicked=false;
			
        }
		});

	
	
	final Button buttonPick = (Button) findViewById(R.id.btnPick);
	buttonPick.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v)
		{ 		
		//	if(PickCounter==0) {show("No Selction To Pick"); return;}
		    PickRoutine();
		    UpdateTitle();
		         
		}
		 
		});
  
  }
  
  /** Holds planet data. */
  private static class Planet {
    private String name = "" ;
    private boolean checked = false ;
    public Planet() {}
    public Planet( String name ) {
      this.name = name ;
    }
    public Planet( String name, boolean checked ) {
      this.name = name ;
      this.checked = checked ;
    }
    public String getName() {
      return name;
    }
    public void setName(String name) {
      this.name = name;
    }
    public boolean isChecked() {
      return checked;
    }
    public void setChecked(boolean checked) {
      this.checked = checked;
    }
    public String toString() {
      return name ; 
    }
    public void toggleChecked() 
    {
      checked = !checked ;
    }
  }
  
  /** Holds child views for one row. */
  private static class PlanetViewHolder {
    private CheckBox checkBox ;
    private TextView textView ;
    public PlanetViewHolder() {}
    public PlanetViewHolder( TextView textView, CheckBox checkBox ) {
      this.checkBox = checkBox ;
      this.textView = textView ;
    }
    public CheckBox getCheckBox() {
      return checkBox;
    }
    public void setCheckBox(CheckBox checkBox) {
      this.checkBox = checkBox;
    }
    public TextView getTextView() {
      return textView;
    }
    public void setTextView(TextView textView) {
      this.textView = textView;
    }    
  }
  
  /** Custom adapter for displaying an array of Planet objects. */
  private class PlanetArrayAdapter extends ArrayAdapter<Planet> {
    
    private LayoutInflater inflater;
    
    public PlanetArrayAdapter( Context context, List<Planet> planetList ) {
      super( context, R.layout.simplerow, R.id.rowTextView, planetList );
      // Cache the LayoutInflate to avoid asking for a new one each time.
      inflater = LayoutInflater.from(context) ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      // Planet to display
      Planet planet = (Planet) this.getItem( position ); 

      // The child views in each row.
      CheckBox checkBox ; 
      TextView textView ; 
      
      // Create a new row view
      if ( convertView == null ) {
        convertView = inflater.inflate(R.layout.simplerow, null);
        
        // Find the child views.
        textView = (TextView) convertView.findViewById( R.id.rowTextView );
        checkBox = (CheckBox) convertView.findViewById( R.id.CheckBox01 );
        
        // Optimization: Tag the row with it's child views, so we don't have to 
        // call findViewById() later when we reuse the row.
        convertView.setTag( new PlanetViewHolder(textView,checkBox) );

        // If CheckBox is toggled, update the planet it is tagged with.
        checkBox.setOnClickListener( new View.OnClickListener()
        {
          public void onClick(View v)
          { 
            CheckBox cb = (CheckBox) v ;
            Planet planet = (Planet) cb.getTag();
           
            if(AlreadyPicked) ///disable clicks if already picked and leave without updating counter
            { cb.setChecked(false); return; }
        
            
            planet.setChecked( cb.isChecked() );
           
            UpdateTitle();
          }
        });        
      }
      // Reuse existing row view
      else {
        // Because we use a ViewHolder, we avoid having to call findViewById().
        PlanetViewHolder viewHolder = (PlanetViewHolder) convertView.getTag();
        checkBox = viewHolder.getCheckBox() ;
        textView = viewHolder.getTextView() ;
           }

      // Tag the CheckBox with the Planet it is displaying, so that we can
      // access the planet in onClick() when the CheckBox is toggled.
      checkBox.setTag( planet ); 
      
      // Display planet data
      checkBox.setChecked( planet.isChecked() );
      textView.setText( planet.getName() );      
      
      return convertView;
    }
    
    
  }
  
  ///boolean c=mainListView.isItemChecked(0);

  private String Increment(String alphaNumericString)
  {  char[] an = alphaNumericString.toCharArray();
     int i = an.length - 1;
      while (true)
      {   if(an[i]<'0' || an[i]>'9') return new String(an); 
      	if (i <= 0)
  			try { throw new Exception("Maxed out number!!!"); }
      	           catch (Exception e)
      	        { e.printStackTrace(); }
  	     	an[i]++;
  	      
      	if (an[i] - 1 == '9')
      	{
      		an[i] = '0';
      		i--;
      		continue;
      	}
      	
      	
      	return new String(an);
      }
  }

  private String Decrement(String alphaNumericString)
  {  char[] an = alphaNumericString.toCharArray();
     int i = an.length - 1;
      while (true)
      {   if(an[i]<'0' || an[i]>'9') return new String(an); 
      	if (i <= 0)
  			try { throw new Exception("Maxed out number!!!"); }
      	           catch (Exception e)
      	        { e.printStackTrace(); }
  	     	an[i]--;
      	if (an[i] == '0'-1)
      	{
      		an[i] = '9';
      		i--;
      		continue;
      	}
      	return new String(an);
      }
  }

  
  
  
private void GetNewRoll()
{   		
	NewNow=false; modified=false;
    final AlertDialog.Builder alert = new AlertDialog.Builder(this);
    alert.setTitle("Create New Batch");
    
    
    final LinearLayout layout = new LinearLayout(this);
    layout.setOrientation(LinearLayout.VERTICAL);
    
    InputFilter[] FilterArray = new InputFilter[2];
    FilterArray[0] = new InputFilter.LengthFilter(15);
    FilterArray[1] = new InputFilter.AllCaps();
    
    final TextView t1=new TextView(this);//"First Seat Number :");
    t1.setText("  Enter First Seat Number");
    
    final EditText input1 = new EditText(this);
	input1.setSingleLine();
	input1.setInputType(InputType.TYPE_CLASS_TEXT);
	//input1.setHint("First Seat No...");
	input1.setText(froll);
	input1.setFilters(FilterArray);
	
	
	final TextView t2=new TextView(this);//"First Seat Number :");
    t2.setText("  Enter Last Seat Number");
	
    final EditText input2 = new EditText(this);
     input2.setSingleLine();
	input2.setInputType(InputType.TYPE_CLASS_TEXT);
	input2.setText(lroll);
	//input2.setHint("Enter Last Seat No...");
	input2.setFilters(FilterArray);
	
	layout.addView(t1);
    layout.addView(input1);
    layout.addView(t2);
    layout.addView(input2);

    
    alert.setView(layout);

    
    alert.setPositiveButton("Create", new DialogInterface.OnClickListener()
	{
	public void onClick(DialogInterface dialog, int whichButton) 
	{
		
/*		String ins1=input1.getText().toString();
	 String ins2=input2.getText().toString();
	if(ins1.length()==0 || ins2.length()==0)
	{   showtop("Invalid Roll");
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	 	imm.hideSoftInputFromWindow(input1.getWindowToken(),0);
    return;
	}
	*/
	 froll = input1.getText().toString();
	 lroll = input2.getText().toString();
	 boolean found=false;
	 //??  froll=in1;lroll=in2;
	   String temproll=froll;
	  //strength=5000;
	/*
	            int i;
			 	Roll.removeAll(Roll);
		     
		        for(i=0;i<strength;i++)
			   	 { Planet tempPlanet=new Planet(temproll);
		        	planetList.add(tempPlanet);
		        	//Roll.add(temproll);
	            	temproll=Increment(temproll);
	            	if(temproll.compareTo(lroll)>0){found=true; break;}
		    	   }
		        if(found) strength=i+1; 
		*/
	        FillList(froll,lroll);
	       AlreadyPicked=false;
	       FileNameWithPath=""; ///fresh file created
			    
			    ((BaseAdapter) mainListView.getAdapter()).notifyDataSetChanged();
			    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
 		 	 	imm.hideSoftInputFromWindow(input1.getWindowToken(),0);
			 	modified=false;
			 	end=false;OpenNow=false;NewNow=false; 	
	 		 	 	
	//	!!! variable reset to force header update from teacher
	//BatchNo="00";Date="";BatchTime="";FileNameWithPath="";
	//  !!! Set Current Date if new file created after old file   
    
	 mainListView.setSelection(0);	 	
	 
      PickCounter=0;
      UpdateTitle(); ///To reset filename and counter display
      GetHeaderDlg();
 	 
 	  }
	});

	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
	{
	  public void onClick(DialogInterface dialog, int whichButton)
	  {
		  InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		 	imm.hideSoftInputFromWindow(input1.getWindowToken(),0);
	    return;
	  }
	});
    alert.show();
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
}

public void FillList(String FirstSeat,String LastSeat)
{
 planetList.removeAll(planetList);
 Roll.removeAll(Roll);
 checkmark.removeAll(checkmark);
 
 String temproll=FirstSeat;
 
 int i=0;
for(i=0;i<maxstrength;i++)
	 { Roll.add(temproll);
	   checkmark.add(Boolean.FALSE);
	   Planet tempPlanet=new Planet(temproll);
 	   planetList.add(tempPlanet);
 	   if(temproll.contains(lroll)) break;
 	   temproll=Increment(temproll); 
	 }
   
 //((BaseAdapter) mainListView.getAdapter()).notifyDataSetChanged();
	
}
  
  public Object onRetainNonConfigurationInstance() 
  {
    return planets ;
  }
  
 public void UpdateTitle()
 {
	 int strength=planetList.size(); //same as roll size;
	 
	 if(AlreadyPicked)
	 {
		 String temp=String.format("[ %02d ] ", PickCounter);
		   setTitle(temp+ProgramName+" - "+FileName); return;
	 }
	 
	 PickCounter=0;
		for(int i=0;i<strength;i++)
		{ 
			Planet planet = listAdapter.getItem(i);
	       if (planet.isChecked()) { PickCounter++; modified=true; } 			
		}
	 
	 
	 
   String temp=String.format("[ %02d ] ", PickCounter);
   setTitle(temp+ProgramName+" - "+FileName);
   
	 
 }

 
 private void GetHeaderDlg()
  {
	 final Dialog myDialog; 
	 myDialog =  new Dialog(this);
	 //myDialog.requestWindowFeature(myDialog.getWindow().FEATURE_NO_TITLE);
	myDialog.setTitle("Batch Details");
	 myDialog.setContentView(R.layout.headerdlg); 
	 myDialog.setCancelable(true); 
	 myDialog.getWindow().getAttributes().width = LayoutParams.FILL_PARENT;
		 	 	
	
	final EditText FBatchNo = (EditText) myDialog.findViewById(R.id.EB_BNO);
	FBatchNo.setSingleLine();
	FBatchNo.setInputType(InputType.TYPE_CLASS_PHONE);
	//FBatchNo.setHint("Batch No 01,02,03 etc...");
	
	FBatchNo.setText(BatchNo); 

	final EditText FTime = (EditText) myDialog.findViewById(R.id.EB_TIME);
	FTime.setText(BatchTime); 
	
	final EditText FSession = (EditText) myDialog.findViewById(R.id.EB_SESSION);
	FSession.setText(BatchSession); 
	
	
	final EditText FDate = (EditText) myDialog.findViewById(R.id.EB_DET);
	FDate.setText(Date);
	
	Button buttoncancel = (Button) myDialog.findViewById(R.id.BCancel); 
	buttoncancel.setOnClickListener(new OnClickListener() { 

	    public void onClick(View v)
	    {myDialog.dismiss();
	     InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		 imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	    } 
	    });
Button buttonok = (Button) myDialog.findViewById(R.id.BCreate); 
buttonok.setOnClickListener(new OnClickListener() { 
 public void onClick(View v) { 


 String ins1=FBatchNo.getText().toString();

	if(ins1.length()!=0 )
	{
	 int batchnumber = new Integer(ins1);
	 tempstr=String.format("%02d", batchnumber);
	 BatchNo=tempstr;
	}
	else  BatchNo="01";
 tempstr=FDate.getText().toString();  Date=tempstr;
 tempstr=FTime.getText().toString();  BatchTime=tempstr;
 tempstr=FSession.getText().toString();  BatchSession=tempstr;
 
  modified=true;		
  
	    
InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
imm.hideSoftInputFromWindow(FBatchNo.getWindowToken(),0);
myDialog.dismiss();
	 } 
}); 

myDialog.show();
InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);  	       	        
  
}


 
 
 private void GetSetDlg()
 {
	 final Dialog myDialog; 
	 myDialog =  new Dialog(this);
	 myDialog.setTitle("Save Batch Preferences");
	 myDialog.setContentView(R.layout.setpreferencesdlg); 
	 myDialog.setCancelable(true); 
	 myDialog.getWindow().getAttributes().width = LayoutParams.FILL_PARENT;
		 	 	

	
	 
	final EditText FSchool = (EditText) myDialog.findViewById(R.id.EB_SCHOOL);
	FSchool.setText(School); 

	
	final EditText FIndex = (EditText) myDialog.findViewById(R.id.EB_INDEX);
	FIndex.setText(Index); 
	
	final EditText FStrim = (EditText) myDialog.findViewById(R.id.EB_STRIM);
	FStrim.setText(Strim); 
	
	final EditText FStandard = (EditText) myDialog.findViewById(R.id.EB_STANDARD);
	FStandard.setText(Standard);
	
	final EditText FSubject = (EditText) myDialog.findViewById(R.id.EB_SUBJECT);
	FSubject.setText(Subject); 
	
	final EditText FSubcode = (EditText) myDialog.findViewById(R.id.EB_SUBCODE);
	FSubcode.setText(SubjectCode);
	
	final EditText FType = (EditText) myDialog.findViewById(R.id.EB_TYPE);
	FType.setText(Type); 

	final EditText FEmail1 = (EditText) myDialog.findViewById(R.id.EB_EMAIL1);
	FEmail1.setText(Email1);
	
	final EditText FEmail2 = (EditText) myDialog.findViewById(R.id.EB_EMAIL2);
	FEmail2.setText(Email2);
	
	final EditText FBatchcreator = (EditText) myDialog.findViewById(R.id.EB_BATCHCREATOR);
	FBatchcreator.setText(BatchCreator);
	
	Button buttoncancel = (Button) myDialog.findViewById(R.id.BtnCancel); 
	buttoncancel.setOnClickListener(new OnClickListener() { 

	    public void onClick(View v)
	    {myDialog.dismiss();
	     InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		 imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	    } 
	    });

Button buttonok = (Button) myDialog.findViewById(R.id.BtnUpdate); 
buttonok.setOnClickListener(new OnClickListener() { 
public void onClick(View v) 
{ 

tempstr=FSchool.getText().toString(); School=tempstr;
tempstr=FIndex.getText().toString();  Index=tempstr;
tempstr=FStrim.getText().toString();  Strim=tempstr;
tempstr=FStandard.getText().toString();  Standard=tempstr;
tempstr=FSubject.getText().toString();  Subject=tempstr;
tempstr=FSubcode.getText().toString(); SubjectCode=tempstr;
tempstr=FType.getText().toString();  Type=tempstr;
tempstr=FEmail1.getText().toString();  Email1=tempstr;
tempstr=FEmail2.getText().toString();  Email2=tempstr;
tempstr=FBatchcreator.getText().toString(); BatchCreator=tempstr;
//??



SharedPreferences settings = getSharedPreferences("BATCH-PREF", 0);
SharedPreferences.Editor editor = settings.edit();
editor.putString("School",School);
editor.putString("Index", Index);
editor.putString("Strim", Strim);
editor.putString("Standard", Standard);
editor.putString("Subject", Subject);
editor.putString("SubjectCode", SubjectCode);
editor.putString("Type", Type);
editor.putString("Email1", Email1);
editor.putString("Email2", Email2);
editor.putString("BatchCreator", BatchCreator);
editor.commit();

 	    
InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
imm.hideSoftInputFromWindow(FSchool.getWindowToken(),0);

myDialog.dismiss();

} 
}); 



myDialog.show();

InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);  	       	        
 
}

 
private void SaveBatchList()
{  
 // if (OpenNow) { OpenFileDialog(); return;}
 // if(NewNow) { GetNewRoll();return; }

 if(BatchNo.length()==0)  {show("Cannot Save. Fill Batch No. In Header");return;}
	
 
    AlertDialog.Builder alert = new AlertDialog.Builder(this);
	alert.setTitle("File Name To Save Batch :");
   	final EditText input = new EditText(this);
	input.setSingleLine();
	input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
	String ShortSubject="";
	if(Subject.length()>3) ShortSubject=Subject.toUpperCase().substring(0,3);
	else ShortSubject=Subject;
	String fylenem=ShortSubject+"-"+BatchNo+"-"+BatchCreator.toUpperCase();
    input.setText(fylenem);
	 
   	alert.setView(input);
	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
	{
	public void onClick(DialogInterface dialog, int whichButton)
	{
	 String fnem = input.getText().toString();
	 fnem+=".bch";
	 FileName=fnem;
	 FileNameWithPath="/sdcard/";
	 FileNameWithPath+=fnem;
	 
	
	 InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	 imm.hideSoftInputFromWindow(input.getWindowToken(),0);
	   	
	 
	 if(fnem.length()==0) { show("Blank File Name"); return;}    
	
	      show(fnem);
	      File file = new File(FileNameWithPath);
	      if(!file.exists()) { SaveList(); modified=false;  FileName=fnem;  UpdateTitle(); return; }
	                 
	      else
	    	  {  OverWriteCase(); }
	       
	     } 
 	
	});

	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	  public void onClick(DialogInterface dialog, int whichButton) {
		  InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		 	imm.hideSoftInputFromWindow(input.getWindowToken(),0);
	//	if (OpenNow)OpenFile();
	//	if(NewNow) GetNewRoll();
	    return;
	  }
	});
	alert.show();
	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
}

  
 private void SaveList()
 {	   
	 
	    if(!AlreadyPicked) { PickRoutine();  }  
	 
	    int i;
 	    modified=false;
 	    String tmpStr;
        String txtData = "\n";
        
        txtData+="School        : ";txtData+=School;       txtData+='\n';
        txtData+="Index         : ";txtData+=Index;        txtData+='\n';
        txtData+="Stream        : ";txtData+=Strim;        txtData+='\n';
        txtData+="Standard      : ";txtData+=Standard;     txtData+='\n';
        txtData+="Subject       : ";txtData+=Subject;      txtData+='\n';
        txtData+="Subject Code  : ";txtData+=SubjectCode;  txtData+='\n';
        txtData+="Type          : ";txtData+=Type;         txtData+='\n';
        txtData+="Batch Number  : ";txtData+=BatchNo;      txtData+='\n';
        txtData+="Batch Creater : ";txtData+=BatchCreator; txtData+='\n';
        txtData+="Email1        : ";txtData+=Email1;       txtData+='\n';
        txtData+="Email2        : ";txtData+=Email2;       txtData+='\n';
        txtData+="Date          : ";txtData+=Date;         txtData+="\n";
        txtData+="Time          : ";txtData+=BatchTime;    txtData+='\n';
        txtData+="Session       : ";txtData+=BatchSession; txtData+="\n";
        txtData+="\n";
        txtData+="=== Reserved Line ====\n";
        txtData+="\n";
    	txtData+="Seat Nos :\n";
    	txtData+="\n";
    	   for(i=0;i<Roll.size();i++) 
     	   { boolean temp=checkmark.get(i);
             if(temp)
                    { //show(Roll.get(i));
    		           txtData+=Roll.get(i);
     	               txtData+='\n';
                      }
     	    }
      	
    	   
    	   //String fnem=Subject.toUpperCase().substring(0,3)+"-"+BatchNo+"-"+BatchCreator+".bch";
    	  
    	 ///  FileNameWithPath is already filled from SaveBatchList()
    		 
     try {
 		File myFile = new File(FileNameWithPath);
 		myFile.createNewFile();
 		FileOutputStream fOut = new FileOutputStream(myFile);
 		OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
 		myOutWriter.append(txtData);
 		myOutWriter.close();
 		fOut.close();
 		modified=false;
 		show("Saved on SD card");
 		if(end) finish();
 		end=false;OpenNow=false;
 		if(NewNow) GetNewRoll();
 				
 	} catch (Exception e) {
 		Toast.makeText(getBaseContext(), e.getMessage(),
 				Toast.LENGTH_SHORT).show();
 	}
    
 }

 private void OverWriteCase()
 {
	 AlertDialog.Builder builder = new AlertDialog.Builder(this);

	 builder.setTitle("File Already Exists");
	 builder.setMessage("Overwrite Old File ?");

	 builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) 
	    {
	         
             SaveList();
             show("Overwritten Old File");
             UpdateTitle();
	         dialog.dismiss();
	         if(end) finish();
	    }

	 });


	 builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) 
	    {

	         // Code that is executed when clicking NO
	    	show("File Not Saved");
	         dialog.dismiss();
	    }

	 });
	 AlertDialog alert = builder.create();
	 alert.show();

 }
 
 
 
 private void SendBatch()
 { 
    String fnem=Subject.toUpperCase().substring(0,3)+"-"+BatchNo+"-"+BatchCreator.toUpperCase();	
 	
 	Intent sendIntent = new Intent(Intent.ACTION_SEND);
 	sendIntent.putExtra(Intent.EXTRA_SUBJECT,fnem);
 	sendIntent.putExtra(Intent.EXTRA_TEXT, "Sending Batch ...");
    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + FileNameWithPath));
 	String E1=Email1.trim();
 	String E2=Email2.trim();
 	if(E1.length()==0 && E2.length()==0) {show("Put Email(s) by [Set] button"); return; }
    String[] emailList={"",""};
 		if(E1.length()!=0) emailList[0]=Email1; 
 		if(E2.length()!=0) emailList[1]=Email2; 
 		sendIntent.putExtra(Intent.EXTRA_EMAIL,emailList);
 		sendIntent.setType("text/plain");
 		startActivity(Intent.createChooser(sendIntent, "Send Mail"));
 	
 }
 
 
 private void OpenFileDialog()
 {	//OpenNow=false;
 	List<String> listItems = new ArrayList<String>();
 	File mfile=new File("/sdcard");
	File[] list=mfile.listFiles();
	  String tempupper;
	     for(int i=0;i<mfile.listFiles().length;i++)
	     {
	      	 tempstr=list[i].getAbsolutePath();
	      	 tempupper=tempstr.toUpperCase();
	      	 if(tempupper.endsWith(".BCH") )
	    	 listItems.add(list[i].getAbsolutePath());
	     }
        
	 final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);
	 
	 AlertDialog.Builder builder = new AlertDialog.Builder(this);
	 builder.setTitle("Select File To Open...");
	 builder.setItems(items, new DialogInterface.OnClickListener()
	 {
	 public void onClick(DialogInterface dialog, int item)
	    {String ttt= (String) items[item];
	      LoadBatch(ttt);
	    }
	 });
	 AlertDialog alert = builder.create();
	 alert.show();
}

 
 
 
 private void LoadBatch(String fylenamewithpath)
 { 
 	try
 	{
 	
 	File myFile = new File(fylenamewithpath);
		FileInputStream fIn = new FileInputStream(myFile);
		BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
		String aDataRow = "";
		
		
		aDataRow=myReader.readLine(); /// blank line separator
	
		String temp[],stemp;
		
		stemp= myReader.readLine();
		temp=stemp.split(":");
		School=temp[1].trim(); 
		
		
		stemp= myReader.readLine();
		temp=stemp.split(":");
		Index=temp[1].trim();
		
		
		stemp= myReader.readLine();
		temp=stemp.split(":");
		Strim=temp[1].trim();
		
		stemp= myReader.readLine();
		temp=stemp.split(":");
		Standard=temp[1].trim();
		
		stemp= myReader.readLine();
		temp=stemp.split(":");
		Subject=temp[1].trim();
		
		stemp= myReader.readLine();
		temp=stemp.split(":");
		SubjectCode=temp[1].trim();
		
		stemp= myReader.readLine();
		temp=stemp.split(":");
		Type=temp[1].trim();
		
		stemp= myReader.readLine();
		temp=stemp.split(":");
		BatchNo=temp[1].trim();
		
		stemp= myReader.readLine();
		temp=stemp.split(":");
		BatchCreator=temp[1].trim();
		
		stemp= myReader.readLine();
		temp=stemp.split(":");
		Email1=temp[1].trim();
		 
		stemp= myReader.readLine();
		temp=stemp.split(":");
		Email2=temp[1].trim();
		
		
		stemp= myReader.readLine();
		temp=stemp.split(":");
		Date=temp[1].trim();
		
		
		stemp= myReader.readLine();
		temp=stemp.split(":");
		BatchTime=temp[1].trim();

		stemp= myReader.readLine();
		temp=stemp.split(":");
		BatchSession=temp[1].trim();
		
		aDataRow=myReader.readLine(); /// blank line separator
		aDataRow=myReader.readLine(); /// ==== Reserved line ===
		aDataRow=myReader.readLine(); /// blank line separator
		aDataRow=myReader.readLine(); /// Seat Nos: Tag
		aDataRow=myReader.readLine(); /// blank line separator
	
		tempRoll.removeAll(tempRoll);
		while ((aDataRow = myReader.readLine()) != null)
			 
  	   	 {
			tempRoll.add(aDataRow);
  	   	  }
		
		myReader.close();
		
		int tot=tempRoll.size();
        PickCounter=tot;
		froll=tempRoll.get(0);
		lroll=tempRoll.get(tot-1);
		
		//show(froll);show(lroll);
		
		FillList(froll,lroll);   ///This removes all arrays and refill them
  	
   
		int totroll=Roll.size();
		int i,j;
		
		for(i=0;i<totroll;i++)
		{ Planet planet = listAdapter.getItem(i);
			for(j=0;j<tot;j++)
				  if(Roll.get(i).contains(tempRoll.get(j))) 
					 { 
	        		       checkmark.set(i,Boolean.TRUE);
					       planet.setChecked(true);
					       checkmark.set(i, true);
					 }
		}
		AlreadyPicked=false;
	   
	
		FileNameWithPath=fylenamewithpath;
 		int start=fylenamewithpath.lastIndexOf("/");
 		
 	    FileName=fylenamewithpath.substring(start+1);
		
		//	 PickRoutine();
		
	
	 mainListView.setSelection(0);
	    UpdateTitle(); ///To reset filename and counter display
	    ((BaseAdapter) mainListView.getAdapter()).notifyDataSetChanged();
		show("Loaded From SD Card");
		 modified=false;
 	}
		catch (Exception e) 
		{
		Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
		}
 }

 
 void PickRoutine()
 { String temproll;
	
 if(AlreadyPicked) 
{   planetList.removeAll(planetList);
for(int i=0;i<Roll.size();i++)
  { 	  
    temproll=Roll.get(i);
     Planet tempPlanet=new Planet(temproll);
      if(checkmark.get(i)) tempPlanet.setChecked(true);

     planetList.add(tempPlanet);
  }
 AlreadyPicked=false;

}



else   
{   ///Detect  checkmarks and save in checkmark array

int strength=planetList.size(); ///planetList=Roll 
  for(int i=0;i<strength;i++)    ////list 
{ 
    Planet planet = listAdapter.getItem(i);
    if (planet.isChecked()) checkmark.set(i,Boolean.TRUE); 
                            else checkmark.set(i,Boolean.FALSE);			
}

  planetList.removeAll(planetList);

/// copy back only ticked seat numbers
  
  for(int i=0;i<Roll.size();i++)
   { 	  
      temproll=Roll.get(i);
     if(checkmark.get(i))
      {        
           Planet tempPlanet=new Planet(temproll);
            planetList.add(tempPlanet);
 	  }
   }
	AlreadyPicked=true;
	
}  ///endof else

 ((BaseAdapter) mainListView.getAdapter()).notifyDataSetChanged();
	 
 }
 
 

 private void showHelp()
   {   
   	 String string = getString(R.string.ht);
   	 WebView wv = new WebView (getBaseContext());
   	 wv.loadData(string, "text/html", "utf-8");
   	 wv.setBackgroundColor(Color.WHITE);
   	 wv.getSettings().setDefaultTextEncodingName("utf-8");
   	
   	 AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
   	 myAlertDialog.setTitle("Help !");
   	 
   	 myAlertDialog.setView(wv);
   	 myAlertDialog.setPositiveButton("Close", new DialogInterface.OnClickListener()
   	 {
   	 public void onClick(DialogInterface arg0, int arg1) {
   	  // do something when the OK button is clicked
   	 }});
   	 myAlertDialog.show();
   }
   

 public void Bye()
 {    	  
	    	 AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
	    	 myAlertDialog.setTitle("Modified !");
	    	 myAlertDialog.setMessage("Save Batch Before Exit ?");
	    	 myAlertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
	    	 {
     	 public void onClick(DialogInterface arg0, int arg1) {
	    	  // do something when the OK button is clicked
	    	     end=true;
     		    SaveBatchList();
	    	         	 
     	 }});
	    	 
	    	 myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	         	 public void onClick(DialogInterface arg0, int arg1) {
              /////do nothing and continue
	 	    	  }});
	    	 
	   	 myAlertDialog.setNeutralButton("No", new DialogInterface.OnClickListener() {
	    	 public void onClick(DialogInterface arg0, int arg1) {
	    	  // do something when the Cancel button is clicked
	    		 finish();
	    	  }});

	    	 myAlertDialog.show();
 }

 public void WarnBeforeOpen()
 {    	  
      	 AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
	    	 myAlertDialog.setTitle("Modified !");
	    	 myAlertDialog.setMessage("Save Batch Before Open ?");
	    	 myAlertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
	    	 {
     	 public void onClick(DialogInterface arg0, int arg1) {
	    	  // do something when the OK button is clicked
	    	     
	    	     OpenNow=true;
     		     SaveBatchList();
   	 
     	 }});
	
	    	 myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	         	 public void onClick(DialogInterface arg0, int arg1) {
              /////do nothing and continue
	 	    	  }});
	    	 
	   	 myAlertDialog.setNeutralButton("No", new DialogInterface.OnClickListener() {
	    	 public void onClick(DialogInterface arg0, int arg1) {
	    	  OpenFileDialog();
	    		 
	    	  }});

	    	 myAlertDialog.show();
	   
 }

 
 public void WarnBeforeNew()
 {    	  
	    	 AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
	    	 myAlertDialog.setTitle("Modified !");
	    	 myAlertDialog.setMessage("Save Batch Before New ?");
	    	 myAlertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
	    	 {
     	 public void onClick(DialogInterface arg0, int arg1) {
	    	  // do something when the OK button is clicked
	    	 NewNow=true;
     		 SaveBatchList();
     	 }});
	    	 myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	         	 public void onClick(DialogInterface arg0, int arg1) {
              /////do nothing and continue
	 	    	  }});
	    	 
	   	 myAlertDialog.setNeutralButton("No", new DialogInterface.OnClickListener() {
	    	 public void onClick(DialogInterface arg0, int arg1) {
	    	  GetNewRoll();
	    		 
	    	  }});

	    	 myAlertDialog.show();
 }

 
 
 
  
}