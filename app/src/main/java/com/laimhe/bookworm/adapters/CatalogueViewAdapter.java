package com.laimhe.bookworm.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laimhe.bookworm.R;
import com.laimhe.bookworm.activities.BookActivity;
import com.laimhe.bookworm.models.Book;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * RecyclerView adapter used to populate Catalogue with books and their information.
 *
 */
public class CatalogueViewAdapter extends RecyclerView.Adapter<CatalogueViewAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Book> books;


    //Adapter constructor
    public CatalogueViewAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public CatalogueViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.catalogue_item, parent, false));
    }

    /**
     * This method is used to populate each RecyclerView element with relevant book information.
     *
     * @param holder RecyclerView element object
     * @param position current element position in the RecyclerView
     */
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull CatalogueViewAdapter.ViewHolder holder, final int position) {
        final Book book = books.get(position);
        assert book != null;

        holder.bookName.setText(book.getAuthor());
        holder.author.setText(book.getName());
        Glide.with(context).load(book.getImage()).into(holder.coverImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, BookActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("productKey", (Serializable) book);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView bookName;
        TextView author;
        ImageView coverImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            coverImage = itemView.findViewById(R.id.product_img);
            bookName = itemView.findViewById(R.id.book_name);
            author = itemView.findViewById(R.id.book_author);
        }
    }
}
