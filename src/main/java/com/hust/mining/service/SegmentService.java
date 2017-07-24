package com.hust.mining.service;

import java.util.List;

public interface SegmentService {

    public String[] getSegresult(String str);

    public List<String[]> getSegresult(List<String> list, int start);

    public List<List<Object>> getResult(List<String[]> list, int index, int start);

    public List<String[]> getSegresult(List<String[]> list, int index, int start);

    public List<String[]> getSegresult(String[] array);
}
