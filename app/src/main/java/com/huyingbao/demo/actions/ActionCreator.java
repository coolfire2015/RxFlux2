package com.huyingbao.demo.actions;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.util.SubscriptionManager;
import com.huyingbao.dm.api.HttpApi;
import com.huyingbao.dm.constant.Constants;
import com.huyingbao.dm.model.HttpResponse;
import com.huyingbao.dm.model.Page;
import com.huyingbao.dm.ui.devicepart.model.DevicePart;
import com.huyingbao.dm.ui.inspection.model.InspectItem;
import com.huyingbao.dm.ui.inspection.model.Inspection;
import com.huyingbao.dm.ui.inspection.model.InspectionTitle;
import com.huyingbao.dm.ui.main.model.Status;
import com.huyingbao.dm.ui.maintenance.model.MaintainItem;
import com.huyingbao.dm.ui.maintenance.model.MaintainItemTitle;
import com.huyingbao.dm.ui.maintenance.model.MaintainPlan;
import com.huyingbao.dm.ui.maintenance.model.MaintainPlanYear;
import com.huyingbao.dm.util.AppUtils;
import com.huyingbao.dm.util.CommonUtils;
import com.huyingbao.dm.util.LocalStorageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * action创建发送管理类
 * Created by liujunfeng on 2017/1/1.
 */
public class ActionCreator extends BaseRxActionCreator implements Actions {
    @Inject
    HttpApi mHttpApi;
    @Inject
    LocalStorageUtils mLocalStorageUtils;

    public ActionCreator(Dispatcher dispatcher, SubscriptionManager manager) {
        super(dispatcher, manager);
        AppUtils.getApplicationComponent().inject(this);
    }

    @Override
    public void postBaseAction(@NonNull String actionId, @NonNull Object... data) {
        postRxAction(newRxAction(actionId, data));
    }

    @Override
    public void login(Context context, String name, String pwd, String channelId) {
        RxAction rxAction = newRxAction(Actions.LOGIN,
                ActionsKeys.NAME, name,
                ActionsKeys.PWD, pwd,
                ActionsKeys.CHANNEL_ID, mLocalStorageUtils.getString(ActionsKeys.CHANNEL_ID, ""));
        postLoadingHttpActionNoCheck(context, rxAction, mHttpApi.login(rxAction.getData()));
    }

    @Override
    public void logout(Context context) {
        RxAction action = newRxAction(Actions.LOGOUT);
        postLoadingHttpActionNoCheck(context, action, mHttpApi.logout());
    }

    /**
     * 获取设备状态列表
     * 合并然后移除
     * 维修待验收和保养待验收
     * 报废和停用
     * 两种状态
     */
    @Override
    public void getDeviceStatusList() {
        RxAction action = newRxAction(Actions.GET_DEVICE_STATUS_LIST);
        Observable<HttpResponse<List<Status>>> httpObservable = mHttpApi.getDeviceStatusList()
                .flatMap(statusListHttpResponse -> {
                    List<Status> statusList = statusListHttpResponse.getResult();
                    if (CommonUtils.isListAble(statusList)) {
                        Status statusCheck = null;
                        Status statusError = null;
                        Iterator<Status> iteratorCheck = statusList.iterator();
                        while (iteratorCheck.hasNext()) {
                            Status status = iteratorCheck.next();
                            if (status.getStatus() == Constants.STATUS_DEVICE_CHECK_REPAIR || status.getStatus() == Constants.STATUS_DEVICE_CHECK_MAINTENANCE) {
                                if (statusCheck == null) statusCheck = new Status();
                                statusCheck.setStatus(Constants.STATUS_DEVICE_CHECK);
                                statusCheck.setCount(statusCheck.getCount() + status.getCount());
                                iteratorCheck.remove();
                            }
                            if (status.getStatus() == Constants.STATUS_DEVICE_DISABLE || status.getStatus() == Constants.STATUS_DEVICE_SCRAP) {
                                if (statusError == null) statusError = new Status();
                                statusError.setStatus(Constants.STATUS_DEVICE_ERROR);
                                statusError.setCount(statusError.getCount() + status.getCount());
                                iteratorCheck.remove();
                            }
                        }
                        if (statusCheck != null) statusList.add(statusCheck);
                        if (statusError != null) statusList.add(statusError);
                    }
                    return Observable.just(statusListHttpResponse);
                });
        postHttpAction(action, httpObservable);
    }

    @Override
    public void getDeviceList(String arrayType, String arrayStatus) {
        RxAction action = newRxAction(Actions.GET_DEVICE_LIST,
                ActionsKeys.ARRAY_TYPE, arrayType,
                ActionsKeys.ARRAY_STATUS, arrayStatus);
        postHttpAction(action, mHttpApi.getDeviceList(action.getData()));
    }

    @Override
    public void getDeviceDetailByCode(Context context, String code) {
        RxAction action = newRxAction(GET_DEVICE_DETAIL_BY_CODE,
                ActionsKeys.CONTEXT, context,
                ActionsKeys.CODE, code);
        postLoadingHttpAction(context, action, mHttpApi.getDeviceDetailByCode(action.getData()));
    }

    @Override
    public void getDeviceDetail(Context context, int deviceId) {
        RxAction action = newRxAction(GET_DEVICE_DETAIL,
                ActionsKeys.ID, deviceId);
        postLoadingHttpAction(context, action, mHttpApi.getDeviceDetail(action.getData()));
    }

    @Override
    public void getDeviceTypeList() {
        RxAction action = newRxAction(Actions.GET_DEVICE_TYPE_LIST);
        postHttpAction(action, mHttpApi.getDeviceTypeList());
    }

    @Override
    public void getDevicePartListByDevice(int deviceId, int page) {
        RxAction action = newRxAction(GET_DEVICE_PART_LIST_BY_DEVICE,
                ActionsKeys.ID, deviceId,
                ActionsKeys.PAGE, page);
        postHttpAction(action, mHttpApi.getDevicePartListByDevice(action.getData()));
    }

    @Override
    public void getDevicePartList(@NonNull String actionType, int id, int page) {
        RxAction action = newRxAction(GET_DEVICE_PART_LIST,
                ActionsKeys.ID, id,
                ActionsKeys.PAGE, page);
        postHttpAction(action, TextUtils.equals(actionType, Actions.GET_DEVICE_PART_LIST_BY_MAINTAIN)
                ? mHttpApi.getDevicePartListByMaintain(action.getData())
                : mHttpApi.getDevicePartListByRepair(action.getData()));
    }

    @Override
    public void getDevicePartListByRepair(int repairId, int page) {
        RxAction action = newRxAction(GET_DEVICE_PART_LIST_BY_REPAIR,
                ActionsKeys.ID, repairId,
                ActionsKeys.PAGE, page);
        postHttpAction(action, mHttpApi.getDevicePartListByRepair(action.getData()));
    }

    @Override
    public void getDevicePartListByMaintain(int maintenanceId, int page) {
        RxAction action = newRxAction(GET_DEVICE_PART_LIST_BY_MAINTAIN,
                ActionsKeys.ID, maintenanceId,
                ActionsKeys.PAGE, page);
        postHttpAction(action, mHttpApi.getDevicePartListByMaintain(action.getData()));
    }

    @Override
    public void getDevicePartListByDeviceType(int machineTypeId) {
        RxAction action = newRxAction(GET_DEVICE_PART_LIST_BY_DEVICE_TYPE,
                ActionsKeys.MACHINE_TYPE_ID, machineTypeId);
        postHttpAction(action, mHttpApi.getDevicePartListByDeviceType(action.getData()));
    }

    @Override
    public void getInspectionListByDevice(int deviceId, int page) {
        RxAction action = newRxAction(GET_INSPECTION_LIST_BY_DEVICE,
                ActionsKeys.ID, deviceId,
                ActionsKeys.PAGE, page);
        postHttpAction(action, mHttpApi.getInspectionListByDevice(action.getData()));
    }

    @Override
    public void getInspectionDetail(Context context, int id) {
        RxAction action = newRxAction(GET_INSPECTION_DETAIL,
                ActionsKeys.ID, id);
        postLoadingHttpAction(context, action, mHttpApi.getInspectionDetail(action.getData()));
    }

    @Override
    public void getInspectionDetailByDevice(Context context, String machineCode) {
        RxAction action = newRxAction(GET_INSPECTION_DETAIL_BY_DEVICE,
                ActionsKeys.MACHINE_CODE, machineCode);
        postLoadingHttpAction(context, action, mHttpApi.getInspectionDetailByDevice(action.getData()));
    }

    @Override
    public void getInspectionListByUser(int page) {
        RxAction action = newRxAction(GET_INSPECTION_LIST_BY_USER,
                ActionsKeys.PAGE, page);
        Observable<HttpResponse<Page<InspectionTitle>>> httpObservable = mHttpApi.getInspectionListByUser(action.getData())
                .flatMap(pageHttpResponse -> {
                    HttpResponse<Page<InspectionTitle>> resultResponse = new HttpResponse<>(pageHttpResponse.getReturnCode(), pageHttpResponse.getMsg());
                    Page<InspectionTitle> maintainItemTitlePage = new Page<>();
                    List<InspectionTitle> maintainItemTitleList = new ArrayList<>();

                    ArrayMap<String, InspectionTitle> arrayMap = new ArrayMap<>();
                    Page<Inspection> result = pageHttpResponse.getResult();
                    List<Inspection> rows = result.getRows();
                    if (CommonUtils.isListAble(rows)) {
                        for (Inspection inspection : rows) {
                            String planDate = inspection.getPlanDate();
                            InspectionTitle maintainItemTitle = arrayMap.get(planDate);
                            if (maintainItemTitle == null) {
                                maintainItemTitle = new InspectionTitle(planDate);
                                maintainItemTitle.addSubItem(inspection);
                                arrayMap.put(planDate, maintainItemTitle);
                            } else {
                                maintainItemTitle.addSubItem(inspection);
                            }
                        }
                        maintainItemTitleList.addAll(arrayMap.values());
                        Collections.reverse(maintainItemTitleList);
                    }
                    maintainItemTitlePage.setRows(maintainItemTitleList);
                    resultResponse.setResult(maintainItemTitlePage);
                    return Observable.just(resultResponse);
                });
        postHttpAction(action, httpObservable);

    }

    @Override
    public void inspectionStart(Context context, int inspectId) {
        RxAction action = newRxAction(INSPECTION_START,
                ActionsKeys.INSPECT_ID, inspectId);
        postLoadingHttpAction(context, action, mHttpApi.inspectionStart(action.getData()));
    }

    @Override
    public void inspectionFinish(Context context, int inspectId, List<InspectItem> items) {
        ArrayMap<Integer, Integer> arrayMap = null;
        if (CommonUtils.isListAble(items)) {
            arrayMap = new ArrayMap<>();
            for (InspectItem inspectItem : items)
                arrayMap.put(inspectItem.getId(), inspectItem.getResult());
        }
        RxAction action = newRxAction(INSPECTION_FINISH,
                ActionsKeys.INSPECT_ID, inspectId,
                ActionsKeys.ITEMS, arrayMap);
        postLoadingHttpAction(context, action, mHttpApi.inspectionFinish(action.getData()));
    }


    @Override
    public void getInspectItemListByInspect(int inspectId) {
        RxAction action = newRxAction(GET_INSPECT_ITEM_LIST_BY_INSPECT,
                ActionsKeys.ID, inspectId);
        postHttpAction(action, mHttpApi.getInspectItemListByInspect(action.getData()));
    }

    @Override
    public void applyRepair(Context context, int deviceId) {
        RxAction action = newRxAction(APPLY_REPAIR,
                ActionsKeys.IDS, deviceId);
        postLoadingHttpAction(context, action, mHttpApi.applyRepair(action.getData()));
    }

    @Override
    public void getRepairListByDevice(int deviceId, int page) {
        RxAction action = newRxAction(GET_REPAIR_LIST_BY_DEVICE,
                ActionsKeys.ID, deviceId,
                ActionsKeys.PAGE, page);
        postHttpAction(action, mHttpApi.getRepairListByDevice(action.getData()));
    }

    @Override
    public void getRepairDetail(Context context, int repairId) {
        RxAction action = newRxAction(GET_REPAIR_DETAIL,
                ActionsKeys.ID, repairId);
        postLoadingHttpAction(context, action, mHttpApi.getRepairDetail(action.getData()));
    }

    @Override
    public void getRepairListByUser(int page) {
        RxAction action = newRxAction(GET_REPAIR_LIST_BY_USER,
                ActionsKeys.PAGE, page);
        postHttpAction(action, mHttpApi.getRepairListByUser(action.getData()));
    }

    @Override
    public void getRepairDetailByDevice(Context context, String machineCode) {
        RxAction action = newRxAction(GET_REPAIR_DETAIL_BY_DEVICE,
                ActionsKeys.MACHINE_CODE, machineCode);
        postLoadingHttpAction(context, action, mHttpApi.getRepairDetailByDevice(action.getData()));
    }

    @Override
    public void repairStart(Context context, int repairId) {
        RxAction action = newRxAction(REPAIR_START,
                ActionsKeys.REPAIR_ID, repairId);
        postLoadingHttpAction(context, action, mHttpApi.repairStart(action.getData()));
    }

    @Override
    public void repairPause(Context context, int repairId, String faultCause, String scheme, String remark, String pauseExplain, List<DevicePart> devicePartList) {
        ArrayMap<Integer, String> spareparts = null;
        if (CommonUtils.isListAble(devicePartList)) {
            spareparts = new ArrayMap<>();
            for (DevicePart devicePart : devicePartList)
                spareparts.put(devicePart.getId(), devicePart.getQuantity());
        }
        RxAction action = newRxAction(REPAIR_PAUSE,
                ActionsKeys.REPAIR_ID, repairId,
                ActionsKeys.FAULT_CAUSE, faultCause,
                ActionsKeys.SCHEME, scheme,
                ActionsKeys.REMARK, remark,
                ActionsKeys.PAUSE_EXPLAIN, pauseExplain,
                ActionsKeys.SPAREPARTS, spareparts);
        postLoadingHttpAction(context, action, mHttpApi.repairPause(action.getData()));
    }

    @Override
    public void repairRestart(Context context, int repairId) {
        RxAction action = newRxAction(REPAIR_RESTART,
                ActionsKeys.REPAIR_ID, repairId);
        postLoadingHttpAction(context, action, mHttpApi.repairRestart(action.getData()));
    }

    @Override
    public void repairFinish(Context context, int repairId, String faultCause, String scheme, String remark, String pauseExplain, List<DevicePart> devicePartList) {
        ArrayMap<Integer, String> spareparts = null;
        if (CommonUtils.isListAble(devicePartList)) {
            spareparts = new ArrayMap<>();
            for (DevicePart devicePart : devicePartList)
                spareparts.put(devicePart.getId(), devicePart.getQuantity());
        }
        RxAction action = newRxAction(REPAIR_FINISH,
                ActionsKeys.REPAIR_ID, repairId,
                ActionsKeys.FAULT_CAUSE, faultCause,
                ActionsKeys.SCHEME, scheme,
                ActionsKeys.REMARK, remark,
                ActionsKeys.PAUSE_EXPLAIN, pauseExplain,
                ActionsKeys.SPAREPARTS, spareparts);
        postLoadingHttpAction(context, action, mHttpApi.repairFinish(action.getData()));
    }

    @Override
    public void repairApprove(Context context, int repairId, String opinion) {
        RxAction action = newRxAction(REPAIR_APPROVE,
                ActionsKeys.REPAIR_ID, repairId,
                ActionsKeys.OPINION, opinion);
        postLoadingHttpAction(context, action, mHttpApi.repairApprove(action.getData()));
    }

    @Override
    public void repairReject(Context context, int repairId, String opinion) {
        RxAction action = newRxAction(REPAIR_REJECT,
                ActionsKeys.REPAIR_ID, repairId,
                ActionsKeys.OPINION, opinion);
        postLoadingHttpAction(context, action, mHttpApi.repairReject(action.getData()));
    }

    @Override
    public void getMaintenanceListByDevice(int deviceId, int page) {
        RxAction action = newRxAction(GET_MAINTENANCE_LIST_BY_DEVICE,
                ActionsKeys.ID, deviceId,
                ActionsKeys.PAGE, page);
        postHttpAction(action, mHttpApi.getMaintenanceListByDevice(action.getData()));
    }

    @Override
    public void getMaintainPlanListByDevice(int deviceId) {
        RxAction action = newRxAction(GET_MAINTAIN_PLAN_LIST_BY_DEVICE,
                ActionsKeys.ID, deviceId);
        Observable<HttpResponse<Page<MaintainPlanYear>>> httpObservable = mHttpApi.getMaintainPlanListByDevice(action.getData())
                .flatMap(pageHttpResponse -> {
                    HttpResponse<Page<MaintainPlanYear>> resultResponse = new HttpResponse<>(pageHttpResponse.getReturnCode(), pageHttpResponse.getMsg());
                    Page<MaintainPlanYear> maintainPlanPage = new Page<>();
                    List<MaintainPlanYear> maintainItemTitleList = new ArrayList<>();

                    ArrayMap<String, MaintainPlanYear> arrayMap = new ArrayMap<>();
                    Page<MaintainPlan> result = pageHttpResponse.getResult();
                    List<MaintainPlan> rows = result.getRows();
                    if (CommonUtils.isListAble(rows)) {
                        for (MaintainPlan maintainPlan : rows) {
                            String year = maintainPlan.getPlanYear();
                            MaintainPlanYear maintainPlanYear = arrayMap.get(year);
                            if (maintainPlanYear == null) {
                                maintainPlanYear = new MaintainPlanYear(year, maintainPlan.getYearPlanId());
                                maintainPlanYear.addSubItem(maintainPlan);
                                arrayMap.put(year, maintainPlanYear);
                            } else {
                                maintainPlanYear.addSubItem(maintainPlan);
                            }
                        }
                        maintainItemTitleList.addAll(arrayMap.values());
                    }
                    maintainPlanPage.setRows(maintainItemTitleList);
                    resultResponse.setResult(maintainPlanPage);
                    return Observable.just(resultResponse);
                });
        postHttpAction(action, httpObservable);
    }

    @Override
    public void getMaintenanceDetail(Context context, int maintenanceId) {
        RxAction action = newRxAction(GET_MAINTENANCE_DETAIL,
                ActionsKeys.ID, maintenanceId);
        postLoadingHttpAction(context, action, mHttpApi.getMaintenanceDetail(action.getData()));
    }

    @Override
    public void getMaintenanceListByUser(int page) {
        RxAction action = newRxAction(GET_MAINTENANCE_LIST_BY_USER,
                ActionsKeys.PAGE, page);
        postHttpAction(action, mHttpApi.getMaintenanceListByUser(action.getData()));
    }

    @Override
    public void getMaintenanceDetailByDevice(Context context, String machineCode) {
        RxAction action = newRxAction(GET_MAINTENANCE_DETAIL_BY_DEVICE,
                ActionsKeys.MACHINE_CODE, machineCode);
        postLoadingHttpAction(context, action, mHttpApi.getMaintenanceDetailByDevice(action.getData()));
    }

    @Override
    public void maintainStart(Context context, int maintainId) {
        RxAction action = newRxAction(MAINTAIN_START,
                ActionsKeys.MAINTAIN_ID, maintainId);
        postLoadingHttpAction(context, action, mHttpApi.maintainStart(action.getData()));
    }

    @Override
    public void maintainPause(Context context, int maintainId, String preExplain, String remark, String pauseExplain, List<MaintainItem> maintainItems, List<DevicePart> devicePartList) {
        ArrayMap<Integer, String> spareparts = null;
        if (CommonUtils.isListAble(devicePartList)) {
            spareparts = new ArrayMap<>();
            for (DevicePart devicePart : devicePartList)
                spareparts.put(devicePart.getId(), devicePart.getQuantity());
        }
        RxAction action = newRxAction(MAINTAIN_PAUSE,
                ActionsKeys.MAINTAIN_ID, maintainId,
                ActionsKeys.PRE_EXPLAIN, preExplain,
                ActionsKeys.REMARK, remark,
                ActionsKeys.PAUSE_EXPLAIN, pauseExplain,
                ActionsKeys.ITEMS, maintainItems,
                ActionsKeys.SPAREPARTS, spareparts);
        postLoadingHttpAction(context, action, mHttpApi.maintainPause(action.getData()));
    }

    @Override
    public void maintainRestart(Context context, int maintainId) {
        RxAction action = newRxAction(MAINTAIN_RESTART,
                ActionsKeys.MAINTAIN_ID, maintainId);
        postLoadingHttpAction(context, action, mHttpApi.maintainRestart(action.getData()));
    }

    @Override
    public void maintainFinish(Context context, int maintainId, String preExplain, String remark, String pauseExplain, List<MaintainItem> maintainItems, List<DevicePart> devicePartList) {
        ArrayMap<Integer, String> spareparts = null;
        if (CommonUtils.isListAble(devicePartList)) {
            spareparts = new ArrayMap<>();
            for (DevicePart devicePart : devicePartList)
                spareparts.put(devicePart.getId(), devicePart.getQuantity());
        }
        RxAction action = newRxAction(MAINTAIN_FINISH,
                ActionsKeys.MAINTAIN_ID, maintainId,
                ActionsKeys.PRE_EXPLAIN, preExplain,
                ActionsKeys.REMARK, remark,
                ActionsKeys.PAUSE_EXPLAIN, pauseExplain,
                ActionsKeys.ITEMS, maintainItems,
                ActionsKeys.SPAREPARTS, spareparts);
        postLoadingHttpAction(context, action, mHttpApi.maintainFinish(action.getData()));
    }

    @Override
    public void maintainApprove(Context context, int maintainId, String opinion) {
        RxAction action = newRxAction(MAINTAIN_APPROVE,
                ActionsKeys.MAINTAIN_ID, maintainId,
                ActionsKeys.OPINION, opinion);
        postLoadingHttpAction(context, action, mHttpApi.maintainApprove(action.getData()));
    }

    @Override
    public void maintainReject(Context context, int maintainId, String opinion) {
        RxAction action = newRxAction(MAINTAIN_REJECT,
                ActionsKeys.MAINTAIN_ID, maintainId,
                ActionsKeys.OPINION, opinion);
        postLoadingHttpAction(context, action, mHttpApi.maintainReject(action.getData()));
    }

    @Override
    public void getMaintainItemListByMaintain(int maintenanceId) {
        RxAction action = newRxAction(GET_MAINTAIN_ITEM_LIST_BY_MAINTAIN,
                ActionsKeys.ID, maintenanceId);
        Observable<HttpResponse<Page<MaintainItemTitle>>> httpObservable = mHttpApi.getMaintainItemListByMaintain(action.getData())
                .flatMap(pageHttpResponse -> {
                    HttpResponse<Page<MaintainItemTitle>> resultResponse = new HttpResponse<>(pageHttpResponse.getReturnCode(), pageHttpResponse.getMsg());
                    Page<MaintainItemTitle> maintainItemTitlePage = new Page<>();
                    List<MaintainItemTitle> maintainItemTitleList = new ArrayList<>();

                    ArrayMap<String, MaintainItemTitle> arrayMap = new ArrayMap<>();
                    Page<MaintainItem> result = pageHttpResponse.getResult();
                    List<MaintainItem> rows = result.getRows();
                    if (CommonUtils.isListAble(rows)) {
                        for (MaintainItem maintainItem : rows) {
                            String position = maintainItem.getPosition();
                            MaintainItemTitle maintainItemTitle = arrayMap.get(position);
                            if (maintainItemTitle == null) {
                                maintainItemTitle = new MaintainItemTitle(position, maintainItem.getResult());
                                maintainItemTitle.addSubItem(maintainItem);
                                arrayMap.put(position, maintainItemTitle);
                            } else {
                                maintainItemTitle.setResult(maintainItemTitle.getResult() && maintainItem.getResult());
                                maintainItemTitle.addSubItem(maintainItem);
                            }
                        }
                        maintainItemTitleList.addAll(arrayMap.values());
                    }
                    maintainItemTitlePage.setRows(maintainItemTitleList);
                    resultResponse.setResult(maintainItemTitlePage);
                    return Observable.just(resultResponse);
                });
        postHttpAction(action, httpObservable);
    }

    @Override
    public void getCheckListByRepair(int repairId, int page) {
        RxAction action = newRxAction(GET_CHECK_LIST_BY_REPAIR,
                ActionsKeys.ID, repairId,
                ActionsKeys.PAGE, page);
        postHttpAction(action, mHttpApi.getCheckListByRepair(action.getData()));
    }

    @Override
    public void getCheckListByMaintain(int maintenanceId, int page) {
        RxAction action = newRxAction(GET_CHECK_LIST_BY_MAINTAIN,
                ActionsKeys.ID, maintenanceId,
                ActionsKeys.PAGE, page);
        postHttpAction(action, mHttpApi.getCheckListByMaintain(action.getData()));
    }

    @Override
    public void getCheckListByUser(Boolean isRepair, int page) {
        RxAction action = newRxAction(GET_CHECK_LIST_BY_USER,
                ActionsKeys.IS_REPAIR, isRepair,
                ActionsKeys.PAGE, page);
        postHttpAction(action, mHttpApi.getCheckListByUser(action.getData()));
    }
}
