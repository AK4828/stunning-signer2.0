package com.meruvian.ldsigner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meruvian.ldsigner.R;
import com.meruvian.ldsigner.entity.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dianw on 8/29/15.
 */
public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.ViewHolder> {
    private Context context;
    private List<Document> documents = new ArrayList<>();

    public DocumentListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_document_detail, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Document document = documents.get(position);
        holder.subject.setText(document.getSubject());
        holder.description.setText(document.getDescription());
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    public void addDocument(Document document) {
        documents.add(document);
        notifyDataSetChanged();;
    }

    public void addDocuments(Collection<Document> documents) {
        this.documents.addAll(documents);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.doc_subject)
        TextView subject;
        @Bind(R.id.doc_description)
        TextView description;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
