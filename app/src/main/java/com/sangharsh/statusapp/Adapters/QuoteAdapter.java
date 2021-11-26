package com.sangharsh.statusapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.sangharsh.statusapp.FullquoteActivity;
import com.sangharsh.statusapp.Model.QuoteModel;
import com.sangharsh.statusapp.R;
import com.sangharsh.statusapp.SinglequoteActivity;

import java.util.List;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.Viewholder> {
    List<QuoteModel> quoteModelList;

    public QuoteAdapter(List<QuoteModel> quoteModelList) {
        this.quoteModelList = quoteModelList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.quote_item,parent,false);
        return new Viewholder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {
        String text = quoteModelList.get(position).getQuote();

        String ssb =quoteModelList.get(position).getQuote();

     //   holder.textView.setText(Html.fromHtml(text.replaceAll("\n", "")));

        holder.textView.setText(Html.fromHtml(text));

      //  holder.textView.setText(Html.fromHtml(ssb));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SinglequoteActivity.class);
                intent.putExtra("quote",holder.textView.getText().toString());
                intent.putExtra("position",holder.getAdapterPosition());
                view.getContext().startActivity(intent);
            }
        });

//        holder.share_direct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent myIntent = new Intent(Intent.ACTION_SEND);
//                myIntent.setType("text/plain");
//                String body = holder.textView.getText().toString();
//                String sub = "Download Status App";
//                myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
//                myIntent.putExtra(Intent.EXTRA_TEXT,body);
//                view.getContext().startActivity(Intent.createChooser(myIntent, "Share Using"));
//            }
//        });
//
//        holder.share_insta.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setPackage("com.instagram.android");
//                shareIntent.setType("text/plain");
//
//                String contentShare = holder.textView.getText().toString();
//                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Download Now:" +
//                       view.getContext().getString(R.string.play_store_link) + view.getContext().getPackageName() + "\n\n" + contentShare + "\n\n" );
//                view.getContext().startActivity(Intent.createChooser(shareIntent, "Share via"));
//
//                try {
//                    view.getContext().startActivity(shareIntent);
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(view.getContext(), "Instagram have not been installed", Toast.LENGTH_SHORT).show();
//                    //ToastHelper.MakeShortText("Whatsapp have not been installed.");
//                }
//                Log.d("TAG", "The interstitial wasn't loaded yet.");
//            }
//        });
//
//        holder.share_wh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setPackage("com.whatsapp");
//                shareIntent.setType("text/plain");
//
//                String contentShare = holder.textView.getText().toString();
//                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Download Now:" +
//                       view.getContext(). getString(R.string.play_store_link) + view.getContext().getPackageName() + "\n\n" + contentShare + "\n\n" );
//                view.getContext().startActivity(Intent.createChooser(shareIntent, "Share via"));
//
//                try {
//                    view.getContext().startActivity(shareIntent);
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(view.getContext(), "Whatsapp have not been installed", Toast.LENGTH_SHORT).show();
//                    //ToastHelper.MakeShortText("Whatsapp have not been installed.");
//                }
//                Log.d("TAG", "The interstitial wasn't loaded yet.");
//            }
//        });
//
//      holder.share_fb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setPackage("com.facebook.orca");
//                shareIntent.setType("text/plain");
//
//                String contentShare = holder.textView.getText().toString();
//                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Download Now:" +
//                        view.getContext().getString(R.string.play_store_link) + view.getContext().getPackageName() + "\n\n" + contentShare + "\n\n" );
//                view.getContext().startActivity(Intent.createChooser(shareIntent, "Share via"));
//
//                try {
//                    view.getContext().startActivity(shareIntent);
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(view.getContext(), "Facebook have not been installed", Toast.LENGTH_SHORT).show();
//                    //ToastHelper.MakeShortText("Whatsapp have not been installed.");
//                }
//                Log.d("TAG", "The interstitial wasn't loaded yet.");
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return quoteModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView textView;
       // ImageButton share_insta, share_fb,share_wh,share_direct;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.quotetext);

//            share_direct = itemView.findViewById(R.id.share_share);
//            share_fb = itemView.findViewById(R.id.share_fb);
//            share_wh = itemView.findViewById(R.id.share_wh);
//            share_insta = itemView.findViewById(R.id.share_insta);
            Typeface custom_font = Typeface.createFromAsset(itemView.getContext().getAssets(), "Roboto-Regular.ttf");
            textView.setTypeface(custom_font);



        }
    }
}
