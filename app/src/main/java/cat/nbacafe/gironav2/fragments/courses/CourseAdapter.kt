package cat.nbacafe.gironav2.fragments.courses

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.nbacafe.gironav2.R
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.ArrayList

class CourseAdapter(private val courseList: ArrayList<Course>,
                    private val clickListener: (String) -> Unit): RecyclerView.Adapter<CourseAdapter.CourseHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseHolder {
        return CourseHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.course_item,
                parent, false)) {
            clickListener(courseList[it].nom)
        }
    }

    override fun onBindViewHolder(holder: CourseHolder, position: Int) {
        val course: Course = courseList[position]
        val storage = FirebaseStorage.getInstance().reference.child(course.nomImatge + ".jpg")
        val imatge = File.createTempFile("img", "jpg")
        storage.getFile(imatge).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(imatge.absolutePath)
            holder.courseImage.setImageBitmap(bitmap)
        }.addOnFailureListener {
            holder.courseImage.setImageResource(R.drawable.basketball_court)
        }
        holder.courseName.text = course.nom
        holder.coursePrice.text = "${course.preu} €"
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    class CourseHolder(itemView: View, clickAtPosition: (Int) -> Unit): RecyclerView.ViewHolder(itemView) {

        val courseName : TextView = itemView.findViewById(R.id.courseNameView)
        val coursePrice : TextView = itemView.findViewById(R.id.coursePriceView)
        val courseImage : ImageView = itemView.findViewById(R.id.courseBg)

        init {
            itemView.setOnClickListener {
                clickAtPosition(adapterPosition)
            }
        }

    }
}