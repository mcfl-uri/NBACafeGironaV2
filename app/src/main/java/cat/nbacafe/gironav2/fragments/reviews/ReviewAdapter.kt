package cat.nbacafe.gironav2.fragments.reviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.nbacafe.gironav2.R

class ReviewAdapter(
    val reviews: List<Review>
) : RecyclerView.Adapter<ReviewAdapter.ReviewHolder>() {

    override fun getItemCount() = reviews.size

    class ReviewHolder(val view: View) :
        RecyclerView.ViewHolder(view) {

        fun bind(review: Review) {

            view.findViewById<TextView>(R.id.productName).text =
                review.producte
            view.findViewById<TextView>(R.id.fullReview).text =
                review.text

        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ReviewHolder(layoutInflater.inflate(R.layout.review_item, parent, false))
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        holder.bind(reviews[position])
    }

}