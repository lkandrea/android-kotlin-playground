package id.lkand.kotlinplayground.activity.dashboard.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import id.lkand.owner.OwnerActivity
import dagger.android.support.DaggerAppCompatActivity
import id.lkand.customer.CustomerActivity
import id.lkand.kotlinplayground.R
import id.lkand.kotlinplayground.databinding.ActivityDashboardBinding
import id.lkand.kotlinplayground.activity.dashboard.viewmodel.DashboardViewModel
import id.lkand.kotlinplayground.activity.main.MainActivity
import id.lkand.kotlinplayground.provider.NavigationProvider
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import kotlin.concurrent.schedule
import javax.inject.Inject

internal class DashboardActivity : DaggerAppCompatActivity() {
    @Inject lateinit var navigationProvider: NavigationProvider
    @Inject lateinit var vmFactory: ViewModelProvider.Factory

    private lateinit var binding: ActivityDashboardBinding
    private val viewModel by lazy {
        ViewModelProviders.of(this, this.vmFactory).get(DashboardViewModel::class.java)
    }

    private val getTrigger = BehaviorSubject.create<Boolean>()
    private val postTrigger = BehaviorSubject.create<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setBinding()
        this.bindView()
    }

    private fun setBinding() {
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        this.viewModel.dashboardModel.observe(this, Observer {
            this.binding.viewmodel = this.viewModel
            this.binding.handler = this
        })
    }

    private fun bindView() {
        Timer().schedule(1000) {
            val didLoad = Observable.just(true)

            this@DashboardActivity.viewModel.transform(
                DashboardViewModel.Input(
                    didLoad,
                    this@DashboardActivity.getTrigger,
                    this@DashboardActivity.postTrigger
                )
            )
        }
    }

    fun handleTapGetButton() {
        this.getTrigger.onNext(true)
    }

    fun handleTapPostButton() {
        this.postTrigger.onNext(true)
    }

    fun handleNavigateButton() {
        this.navigationProvider.navigate(this, MainActivity::class.java)
        this.finish()
    }

    fun handleCustomerButton() {
        this.navigationProvider.navigate(this, CustomerActivity::class.java)
    }

    fun handleOwnerButton() {
        this.navigationProvider.navigate(this, OwnerActivity::class.java)
    }

}