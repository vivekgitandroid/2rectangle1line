package com.example.administrator.a2rectangle1line;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import com.example.administrator.a2rectangle1line.DrawBox.*;

import static com.example.administrator.a2rectangle1line.DrawBox.BOXES_LIMIT;

public class MainActivity extends AppCompatActivity {


    static int btn_no=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //create Boxes button
        final Button boxes_btn=(Button)findViewById(R.id.boxes_btn);
        boxes_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //set visibility of box and int button as true
                //set clickability of all other buttons as false??
                Button box_btn=(Button)findViewById(R.id.box_btn);
                Button int_btn=(Button)findViewById(R.id.int_btn);
                Button rels_btn=(Button)findViewById(R.id.rels_btn);
                if(box_btn.getVisibility()==View.GONE) {
                    box_btn.setVisibility(View.VISIBLE);
                    int_btn.setVisibility(View.VISIBLE);
                    //boxes_btn.setVisibility(View.VISIBLE);
                    rels_btn.setVisibility(View.INVISIBLE);
                }
                else {
                    box_btn.setVisibility(View.GONE);
                    int_btn.setVisibility(View.GONE);
                    //boxes_btn.setVisibility(View.VISIBLE);
                    rels_btn.setVisibility(View.VISIBLE);
                }
            }
        });

        //create Relations button
        final Button rels_btn=(Button)findViewById(R.id.rels_btn);
        rels_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Button binary_rels_btn=(Button)findViewById(R.id.binary_rels_btn);
                Button n_ary_rels_btn=(Button)findViewById(R.id.n_ary_rels_btn);

                Button boxes_btn=(Button)findViewById(R.id.boxes_btn);
                if(binary_rels_btn.getVisibility()==View.GONE) {
                    binary_rels_btn.setVisibility(View.VISIBLE);
                    n_ary_rels_btn.setVisibility(View.VISIBLE);
                    boxes_btn.setVisibility(View.INVISIBLE);
                }
                else {
                    binary_rels_btn.setVisibility(View.GONE);
                    n_ary_rels_btn.setVisibility(View.GONE);
                    boxes_btn.setVisibility(View.VISIBLE);
                }
            }
        });

    //create Binary Relations button
        final Button binary_rels_btn=(Button)findViewById(R.id.binary_rels_btn);
        binary_rels_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Button bin_ass_btn=(Button)findViewById(R.id.bin_ass_btn);
                Button bin_comp_btn=(Button)findViewById(R.id.bin_comp_btn);
                Button bin_inh_btn=(Button)findViewById(R.id.bin_inh_btn);
                Button bin_rea_btn=(Button)findViewById(R.id.bin_rea_btn);

                Button n_ary_rels_btn=(Button)findViewById(R.id.n_ary_rels_btn);
                Button rels_btn=(Button)findViewById(R.id.rels_btn);

                if(bin_ass_btn.getVisibility()==View.GONE) {
                    bin_ass_btn.setVisibility(View.VISIBLE);
                    bin_comp_btn.setVisibility(View.VISIBLE);
                    bin_inh_btn.setVisibility(View.VISIBLE);
                    bin_rea_btn.setVisibility(View.VISIBLE);

                    n_ary_rels_btn.setVisibility(View.INVISIBLE);
                    rels_btn.setVisibility(View.INVISIBLE);

                }
                else {
                    bin_ass_btn.setVisibility(View.GONE);
                    bin_comp_btn.setVisibility(View.GONE);
                    bin_inh_btn.setVisibility(View.GONE);
                    bin_rea_btn.setVisibility(View.GONE);

                    n_ary_rels_btn.setVisibility(View.VISIBLE);
                    rels_btn.setVisibility(View.VISIBLE);
                }
            }
        });

        //create N-ary Relations button
        final Button n_ary_rels_btn=(Button)findViewById(R.id.n_ary_rels_btn);
        n_ary_rels_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Button n_ary_ass_btn=(Button)findViewById(R.id.n_ary_ass_btn);
                Button n_ary_comp_btn=(Button)findViewById(R.id.n_ary_comp_btn);
                Button n_ary_inh_btn=(Button)findViewById(R.id.n_ary_inh_btn);
                Button n_ary_rea_btn=(Button)findViewById(R.id.n_ary_rea_btn);

                Button binary_rels_btn=(Button)findViewById(R.id.binary_rels_btn);
                Button rels_btn=(Button)findViewById(R.id.rels_btn);

                if(n_ary_ass_btn.getVisibility()==View.GONE) {
                    n_ary_ass_btn.setVisibility(View.VISIBLE);
                    n_ary_comp_btn.setVisibility(View.VISIBLE);
                    n_ary_inh_btn.setVisibility(View.VISIBLE);
                    n_ary_rea_btn.setVisibility(View.VISIBLE);

                    binary_rels_btn.setVisibility(View.INVISIBLE);
                    rels_btn.setVisibility(View.INVISIBLE);

                }
                else {
                    n_ary_ass_btn.setVisibility(View.GONE);
                    n_ary_comp_btn.setVisibility(View.GONE);
                    n_ary_inh_btn.setVisibility(View.GONE);
                    n_ary_rea_btn.setVisibility(View.GONE);

                    binary_rels_btn.setVisibility(View.VISIBLE);
                    rels_btn.setVisibility(View.VISIBLE);

                }
            }
        });

        //create Box button
        Button box_btn=(Button)findViewById(R.id.box_btn);
        box_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                BoxArea newBox = new BoxArea(50, 350, 50, 250, btn_no++);

                DrawBox db = (DrawBox)findViewById(R.id.db_id);
                if (db.mBoxes.size() == db.BOXES_LIMIT) {
                    //???Log.w(TAG, "Clear all boxes, size is " + db.mBoxes.size());
                    // remove first box
                    db.mBoxes.clear();
                }

                //????Log.w(TAG, "Added box " + touchedBox);
                db.mBoxes.add(newBox);
                db.invalidate();
                //???how to add this box to mboxpointer???

            }
        });


        final Button bin_ass_btn = (Button) findViewById(R.id.bin_ass_btn);
        bin_ass_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DrawBox db = (DrawBox)findViewById(R.id.db_id);
                bin_ass_btn.setClickable(false);
                db.invalidate();


            }
        });

        final Button bin_comp_btn = (Button) findViewById(R.id.bin_comp_btn);
        bin_comp_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DrawBox db = (DrawBox)findViewById(R.id.db_id);
                bin_comp_btn.setClickable(false);
                db.invalidate();

            }
        });

    }
}


//make dotted block for interface??
///make delete button for block and relation/line!!!!!!
//set buttons unclickable when creating relation
//make two buttons binary and n ary relations. in binary there will be association(line), composition(block inside block), inheritance(block replaces old block), realisation(block realises an interface)
//nary button will have....


//composition wala prob?????
//re-add border mrectangle
//zoom done, do pan
//box cannot go outside

//button color
//set size of buttons wala linear layout



/*problems encounters:
when ondraw returns tru(ie handled) only then pinch gesture is detected!!!
when we scale view, getx and gety have to be modified too. i.e getx/mscalefactor
scroll bar??? panning?? focus/translation??
composition.. inside block should be less than half the size of outer block
how to dynamically change lines when we move block?? save all connections(like ass, comp)in boxarea, so when box is moved, we call ondraw on all these connections also



///next tasks:
ask for class name
when class name provided, created new button for this block in classes section
when connecting blocks, ask for directivity/multiplicity and name of relation etc



 */
