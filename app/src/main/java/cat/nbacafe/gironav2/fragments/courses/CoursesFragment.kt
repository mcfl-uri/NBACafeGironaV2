package cat.nbacafe.gironav2.fragments.courses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.nbacafe.gironav2.R
import cat.nbacafe.gironav2.common.SharedViewModel
import cat.nbacafe.gironav2.databinding.FragmentCoursesBinding
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*

class CoursesFragment : Fragment() {

    private lateinit var crv: RecyclerView
    private lateinit var courses: ArrayList<Course>
    private lateinit var myAdapter: CourseAdapter
    private lateinit var db: FirebaseFirestore
    val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCoursesBinding>(
            inflater,
            R.layout.fragment_courses, container, false
        )

        crv = binding.recyclerCourses
        crv.layoutManager = LinearLayoutManager(context)
        crv.setHasFixedSize(true)

        courses = arrayListOf()

        myAdapter = CourseAdapter(courses) {
            sharedViewModel.setItem(it)
            view?.findNavController()?.navigate(R.id.action_coursesFragment_to_newReviewFragment)
        }

        crv.adapter = myAdapter

        EventChangeListener()

        binding.backFromCoursesBtn.setOnClickListener { View ->
            view?.findNavController()?.navigate(R.id.action_coursesFragment_to_homeFragment)
        }

        return binding.root
    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("plat")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Error de Firestore", error.message.toString())
                        return
                    }
                    for (dc : DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            courses.add(dc.document.toObject(Course::class.java))
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }

            })
    }

}