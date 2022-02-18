package cat.nbacafe.gironav2.fragments.reviews

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.nbacafe.gironav2.R
import cat.nbacafe.gironav2.databinding.FragmentUserReviewsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*

class UserReviewsFragment : Fragment() {

    private lateinit var rrv: RecyclerView
    private lateinit var reviews: ArrayList<Review>
    private lateinit var myAdapter: ReviewAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentUserReviewsBinding>(
            inflater,
            R.layout.fragment_user_reviews, container, false
        )

        auth = FirebaseAuth.getInstance()

        rrv = binding.recyclerReviews
        rrv.layoutManager = LinearLayoutManager(context)
        rrv.setHasFixedSize(true)

        reviews = arrayListOf()

        myAdapter = ReviewAdapter(reviews)

        rrv.adapter = myAdapter

        EventChangeListener()

        binding.backFromReviewsBtn.setOnClickListener { View ->
            view?.findNavController()?.navigate(R.id.action_userReviewsFragment_to_homeFragment)
        }

        return binding.root
    }

    private fun EventChangeListener() {
        auth = FirebaseAuth.getInstance()
        val alias = auth.currentUser?.email.toString()
        db = FirebaseFirestore.getInstance()
        db.collection("client/$alias/review")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Error de Firestore", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            reviews.add(dc.document.toObject(Review::class.java))
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }

            })
    }

}