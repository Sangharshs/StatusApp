package com.sangharsh.statusapp.Adapters;

import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sangharsh.statusapp.Model.EnglishQuoteModel;
import com.sangharsh.statusapp.Model.QuoteModel;
import com.sangharsh.statusapp.R;
import com.sangharsh.statusapp.SingleEnglishQuoteActivity;
import com.sangharsh.statusapp.SinglequoteActivity;

import java.util.List;

public class EnglishQuoteAdapter  extends RecyclerView.Adapter<EnglishQuoteAdapter.Viewholder> {
    List<EnglishQuoteModel> quoteModelList;


    public EnglishQuoteAdapter(List<EnglishQuoteModel> quoteModelList) {
        this.quoteModelList = quoteModelList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.quote_item,parent,false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {
       String text = quoteModelList.get(position).getQuote();
        String adjusted = text.replaceAll("(?m)^[ \t]*\r?\n", "");
        holder.textView.setText(Html.fromHtml(text.replaceAll("\r\n", "")));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SingleEnglishQuoteActivity.class);
                intent.putExtra("quote",holder.textView.getText().toString());
                intent.putExtra("position",holder.getAdapterPosition());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quoteModelList.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageButton share_insta, share_fb,share_wh,share_direct;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.quotetext);
            share_direct = itemView.findViewById(R.id.share_share);
            share_fb = itemView.findViewById(R.id.share_fb);
            share_wh = itemView.findViewById(R.id.share_wh);
            share_insta = itemView.findViewById(R.id.share_insta);

            Typeface custom_font = Typeface.createFromAsset(itemView.getContext().getAssets(), "Roboto-Regular.ttf");
            textView.setTypeface(custom_font);
        }
    }
}
