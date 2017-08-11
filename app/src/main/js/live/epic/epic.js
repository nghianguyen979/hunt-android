/**
 * Created by daniel on 8/5/17.
 */

import {
    FETCH_EVENT_SUCCESS, FETCH_EVENT_FAIL, FETCH_EVENT_LOADING, LOAD, LOAD_MORE, LoadResult,
    FETCH_MORE_EVENT_LOADING, FETCH_MORE_EVENT_SUCCESS, FETCH_MORE_EVENT_FAIL
} from "../action/action";
import * as apiClient from "../model/apiClient";
import 'rxjs/add/operator/mergeMap';
import Rx from "rxjs";

export const fetchEventEpic = action$ =>
    action$.filter(action => action.type === LOAD)
        .mergeMap(action =>
            apiClient.getEvents(action.date, 0)
                .map(response => response.live_events)
                .map(content => LoadResult(FETCH_EVENT_SUCCESS, content))
                .catch(error => Rx.Observable.of({
                    type: FETCH_EVENT_FAIL,
                    payload: error
                }))
                .startWith(LoadResult(FETCH_EVENT_LOADING, null))
        );

export const fetchMoreEventEpic = (action$, store) =>
    action$.filter(action => action.type === LOAD_MORE)
        .mergeMap(action => {
            let nextOffset = store.getState().event.offset + 20;
            return apiClient.getEvents(action.date, nextOffset)
                .map(response => response.live_events)
                .map(content => LoadResult(FETCH_MORE_EVENT_SUCCESS, content))
                .catch(error => Rx.Observable.of({
                    type: FETCH_MORE_EVENT_FAIL,
                    payload: error
                }))
                .startWith(LoadResult(FETCH_MORE_EVENT_LOADING, null))
        });