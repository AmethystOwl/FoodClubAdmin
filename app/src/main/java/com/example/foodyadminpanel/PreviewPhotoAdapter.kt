package com.example.foodyadminpanel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodyadminpanel.databinding.ItemPreviewPhotoBinding

class PreviewPhotoAdapter( val arrayList: ArrayList<String>, private val deletePreviewItem: DeletePreviewItem) :
    RecyclerView.Adapter<PreviewPhotoAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemPreviewPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_preview_photo, parent, false)
                return ViewHolder(ItemPreviewPhotoBinding.bind(view))
            }
        }

        fun bind(imageUrl: String, position: Int, deletePreviewItem: DeletePreviewItem) {
            binding.imageUrl = imageUrl
            binding.onDeleteClickListener = deletePreviewItem
            binding.position = position
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrayList[position], position, deletePreviewItem)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class DeletePreviewItem(private val onClick: (String, Int) -> Unit) {
        fun onItemClick(previewImageUrl: String, position: Int) = onClick(previewImageUrl, position)
    }
}
