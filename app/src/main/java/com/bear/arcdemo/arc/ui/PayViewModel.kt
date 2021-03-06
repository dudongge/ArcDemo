package com.bear.arcdemo.arc.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bear.arcdemo.algorithm.method.BackTrack
import com.bear.arcdemo.algorithm.method.Fraction
import com.bear.arcdemo.algorithm.sort.QuickSort
import com.bear.arcdemo.algorithm.sort.Sort
import com.bear.arcdemo.algorithm.sort.SortDynamicProxy
import com.bear.arcdemo.arc.data.PayRepository
import com.bear.arcdemo.arc.data.Result
import com.bear.arcdemo.arc.data.bearLog
import com.bear.arcdemo.arc.data.model.PayInfo
import com.bear.arcdemo.kot.KotOperation
import com.bear.arcdemo.showcode.*
import com.bear.arcdemo.source.multhread.ThreadAwait
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class PayViewModel(private val payRepository: PayRepository) : ViewModel() {

    private val _payResult = MutableLiveData<PayResult>()
    val payResult: MutableLiveData<PayResult> = _payResult

    fun toPay(amount: Int) {
        viewModelScope.launch {
            when (payRepository.toPay(amount)) {
                is Result.Success<PayInfo> -> _payResult.value =
                    PayResult(success = PayForUser("支付成功"))
                else -> _payResult.value = PayResult(errorCode = -1)
            }
        }

    }

    fun sort(array: Array<Int>): Array<Int> {
//        val sort = SortDynamicProxy(BubbleSort()).newProxyInstance() as Sort
        val sort = SortDynamicProxy(QuickSort()).newProxyInstance() as Sort
        return sort.sort(array)
    }

    //回溯算法
    fun backTrace() {
        val input = intArrayOf(1, 2, 3)
        val backTrack = BackTrack()
        backTrack.permute(input)
    }

    fun subsets() {
        val input = intArrayOf(1, 2, 3)
        val backTrack = BackTrack()

        val input2 = intArrayOf(1, 2, 2)
        val ret2 = backTrack.subsetsWithDup(input2)

        val ret1 = backTrack.subsets(input)
    }

    fun mulThread() {
        val sb = StringBuffer()
        val ctl = CountDownLatch(10)
        for (i in 0 until 10) {
//            MulThread("thread_$i").start()
            ThreadAwait(runnable = { sb.append(i).append("-") }, "thread_$i", ctl).start()
        }
        ctl.await(3, TimeUnit.SECONDS)
        bearLog("sb--> <${sb}>")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun download() {
        for (i in 0..15) {
            DownLoadManager.instance.addTask(
                DownloadTask(
                    "https://hbimg.huabanimg.com/e7aed7a6b8cb9f561d212176fd2094742e006938124ca-1lFwAm_fw658/format/webp",
                    "pic${i}"
                )
            )
        }
//        DownLoadManager.instance.addTask(
//            DownloadTask(
//                "https://hbimg.huabanimg.com/e7aed7a6b8cb9f561d212176fd2094742e006938124ca-1lFwAm_fw658/format/webp",
//                "picProgress"
//            )
//        ) {
//            bearLog("task<picProgress> progress is $it")
//        }
    }

    fun fraction() {
        Fraction().simplifiedFractions(3)
    }

    fun buyTicket() {
        for (i in 0..100) {
            Thread({ TrainTicket().sell() }, "thread_$i").start()

        }
    }

    fun kotOperation() {
        KotOperation().main()
    }

}