package com.meruvian.ldsigner.entity.user;

import java.io.Serializable;

/**
 * @author Dian Aditya
 *
 */
public class Name implements Serializable {
    private String prefix;
    private String first;
    private String middle;
    private String last;

    public Name() {}

    /**
     * @param prefix
     * @param first
     * @param middle
     * @param last
     */
    public Name(String prefix, String first, String middle, String last) {
        this.prefix = prefix;
        this.first = first;
        this.middle = middle;
        this.last = last;
    }

    public Name(Name name) {
        this(name.getPrefix(), name.getFirst(), name.getMiddle(), name.getLast());
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix
     *            the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return the first
     */
    public String getFirst() {
        return first;
    }

    /**
     * @param first
     *            the first to set
     */
    public void setFirst(String first) {
        this.first = first;
    }

    /**
     * @return the middle
     */
    public String getMiddle() {
        return middle;
    }

    /**
     * @param middle
     *            the middle to set
     */
    public void setMiddle(String middle) {
        this.middle = middle;
    }

    /**
     * @return the last
     */
    public String getLast() {
        return last;
    }

    /**
     * @param last
     *            the last to set
     */
    public void setLast(String last) {
        this.last = last;
    }
}
