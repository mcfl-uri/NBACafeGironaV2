package cat.nbacafe.gironav2.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.nbacafe.gironav2.fragments.courses.Course

class SharedViewModel : ViewModel() {
    private lateinit var selectedItem: Course

    fun setItem(item: Course) {
        selectedItem = item
    }

    fun getItem() : Course {
        return selectedItem
    }
}