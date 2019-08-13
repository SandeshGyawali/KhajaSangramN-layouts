package com.example.khajasangram.Adaptors;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khajasangram.MenudisplayActivity;
import com.example.khajasangram.R;
import com.example.khajasangram.SQLite.Databasehelper;
import com.example.khajasangram.SubMenuItemAdaptor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuItemAdaptor extends RecyclerView.Adapter<MenuItemAdaptor.MenuItemViewHolder> {

    RecyclerView recyclerView;
    Context context;
     ArrayList<String> list;
     ArrayList<String> submenuitem_list ;
     ArrayList<String> submenuitem_pricelist;
    ArrayList<String> submenuitem_list1 ;
     ArrayList<String> submenuitem_pricelist1 ;
     DatabaseReference reference;
     String id;
    int submenu_item_index ;
    int submenu_item_index_first;
    int submenu_item_index1 ;
    SubMenuItemAdaptor subMenuItemAdaptor, first_adapter;
    DatabaseReference reference1;
    LinearLayout linearLayout;
    TextView txt1,txt2 = null;
    int check = 0;
    Databasehelper db;
    LinearLayout layout;



    public MenuItemAdaptor(Context context, ArrayList<String> list, String id,RecyclerView recyclerView)
    {
        this.context = context;
        this.list = list;
        this.id = id;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.menu_item_list,parent,false);

        if(check == 0) {
            setSubmenuofFirstItem();
            check++;
        }

        return new MenuItemAdaptor.MenuItemViewHolder(view);
    }

    private void setSubmenuofFirstItem() {
        DatabaseReference reference;

        submenu_item_index_first =0;
        submenuitem_list1 = new ArrayList<>();
        submenuitem_pricelist1 = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Menu").child(id).child(list.get(0));
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //  Toast.makeText(context,"count= "+dataSnapshot.getChildrenCount(),Toast.LENGTH_SHORT).show();
                for(int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    String name = dataSnapshot.child("name" + submenu_item_index_first).child("name").getValue(String.class);
                    String price = dataSnapshot.child("name" + submenu_item_index_first).child("price").getValue(String.class);

                    submenu_item_index_first++;

                    submenuitem_list1.add(name);
                    submenuitem_pricelist1.add(price);
                    first_adapter = new SubMenuItemAdaptor(context, submenuitem_list1, submenuitem_pricelist1);

                    recyclerView.setAdapter(first_adapter);

                    recyclerView.setHasFixedSize(true);

                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(mLayoutManager);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {

        holder.item_name.setText(list.get(position));
        linearLayout = holder.menu_item;

        holder.menu_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt1 = holder.item_name;
                txt1.setTextColor(Color.parseColor("#FDFCFD"));
                if(txt2 != null) {
                    txt2.setTextColor(Color.parseColor("#0A0A0A"));
                }
                linearLayout.setBackgroundResource(R.drawable.rec_box_rounded_corner);
                //holder.menu_item.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                holder.menu_item.setBackgroundResource(R.drawable.menu_item_pressed);

                txt2 = txt1;
                linearLayout = holder.menu_item;
                reference = FirebaseDatabase.getInstance().getReference("Menu").child(id).child(list.get(position));

                //when new item is pressed the string array should be initialized
                //again to new list of strings
                //and also the index should start from 0
                submenuitem_list = new ArrayList<>();
                submenuitem_pricelist = new ArrayList<>();
                submenu_item_index = 0;
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Toast.makeText(context,"count item= "+dataSnapshot.getChildrenCount(),Toast.LENGTH_SHORT).show();


                        for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                                String subitem = dataSnapshot.child("name" + submenu_item_index).child("name").getValue(String.class);
                                String subitem_price = dataSnapshot.child("name" + submenu_item_index).child("price").getValue(String.class);


                                submenu_item_index++;

                                submenuitem_list.add(subitem);
                                submenuitem_pricelist.add(subitem_price);

                                subMenuItemAdaptor = new SubMenuItemAdaptor(context, submenuitem_list, submenuitem_pricelist);

                                recyclerView.setAdapter(subMenuItemAdaptor);

                                recyclerView.setHasFixedSize(true);

                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
                                recyclerView.setLayoutManager(mLayoutManager);
                            }
                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MenuItemViewHolder extends RecyclerView.ViewHolder {
        TextView item_name;
        LinearLayout menu_item;

        public MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);

            item_name = itemView.findViewById(R.id.food_name);
            menu_item = itemView.findViewById(R.id.menu_item);
        }
    }
}
