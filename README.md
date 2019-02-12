# Simple-text-styling-Android
Simple-text-styling-Android-
A simple framework for text styling in android

# The problem
In android its not very simple to manipulate text in a simple EditText without
using a special library or some form of richtext xml widget that consumes large amount of
space and makes all text editors similar .This app does just that a simple customization text
editing method exists that accepts simple parameters of type or size of span 
and both the **start** and **end** of the spanned text.

>  For the size parameter :  
> **-1** makes text bold  
> **-2** Highlights text provided the color is in  integer hicolor  
> **-3** underline  
> **-4** italic  
> **1** decrease text size by amount **var**  
> **2** decrease text size by amount **var** 
> (**any size**) changes text to that size



After thorough testing and investigation on text behavior in android this method is the end result
```java
    private void Spanning(int size, int ss, int se) {//Styling
            int st = m.getSelectionStart();
            int end = m.getSelectionEnd();
            int ind = -1;
            int oldei = -1;
    
            m.clearComposingText();
            int maindiff = 0;
            int colororig = hicolor;
    
            int colorchang = 0;
            String separation = "0";
    
            SpannableString selection = new SpannableString(m.getText().subSequence(ss, se));//selected part
            SpannableString selectionl = new SpannableString("");
            SpannableString selectionr = new SpannableString("");
            SpannedString newparts = new SpannedString("");
            AbsoluteSizeSpan[] spannedsize = m.getText().getSpans(ss, se, AbsoluteSizeSpan.class);
            StyleSpan[] spannedsize2 = m.getText().getSpans(ss, se, StyleSpan.class);
            BackgroundColorSpan[] spannedsize3 = m.getText().getSpans(ss, se, BackgroundColorSpan.class);
            UnderlineSpan[] spannedsize4 = m.getText().getSpans(ss, se, UnderlineSpan.class);
            //abs size spans selection
            int v = -1;
    
            if (size < 0) {
                for (int xc = 0; size == -2 ? xc < spannedsize3.length : size == -3 ? xc < spannedsize4.length : xc < spannedsize2.length; xc++) {//check removal
                    int sps = m.getText().getSpanStart(size == -2 ? spannedsize3[xc] : size == -3 ? spannedsize4[xc] : spannedsize2[xc]);
                    int spe = m.getText().getSpanEnd(size == -2 ? spannedsize3[xc] : size == -3 ? spannedsize4[xc] : spannedsize2[xc]);//endinf index
                    if (size == -2) {
                        colororig = spannedsize3[xc].getBackgroundColor();
                        if (colororig != hicolor) {
                            colorchang = 1;
                        }
                    } else if (size == -4 || size == -1) {
                        int vcheck = spannedsize2[xc].getStyle();
                        if ((size == -4 && vcheck == Typeface.BOLD || size == -1 && vcheck == Typeface.ITALIC)) {
    
                            if ((ss == sps && se == spe)) {
                                ind = 1;
                            } else
                                ind = -2;// difference exists
    
                        }
                    }
                    if (!(ss == sps && se == spe)) {
                        if (ss < spe && sps < ss && se>spe ) {// left spanned part within selection
                            maindiff = maindiff + (spe - ss);
                        } else if (se < spe && sps < se && ss<sps ) {//right //
                            maindiff = maindiff + (se - sps);
                        } else if(se-ss>spe-sps){
                        maindiff = maindiff + (spe - sps);// in selection
                    }else
                        maindiff = maindiff + (se - ss);// outside selection
                    } else {
                        maindiff = (se - ss);
                        break;
                    }
    
                }
            }
            int sstyle = 0, shigh = 0, sunder = 0;
            int style = 0, high = 0, under = 0, abs = 0;
    
            int totallength = spannedsize.length + spannedsize2.length + spannedsize3.length + spannedsize4.length;
            int actualtotallength = totallength;
    
            for (int sp = 0; sp < totallength; sp++) {//for inside word
                int pst = -1;
                int pend = -1;
                int subvar = -1;
                int si = -1, ei = -1, si1 = -1, ei1 = -1, si2 = -1, ei2 = -1, si3 = -1, ei3 = -1;
                if (spannedsize.length > (sp + (high + style + under))) {
    
                    si = m.getText().getSpanStart(spannedsize[sp + (high + style + under)]);
                    ei = m.getText().getSpanEnd(spannedsize[sp + (high + style + under)]);//endinf index
    
    
                    CurrentSize = spannedsize[sp + (high + style + under)].getSize();
    
    
                    pst = si;
                    pend = ei;
    
    
                }
                if (spannedsize4.length > sunder + (sp + (high + style + abs))) {
    
                    si3 = m.getText().getSpanStart(spannedsize4[sunder + (sp + (high + style + abs))]);
                    ei3 = m.getText().getSpanEnd(spannedsize4[sunder + (sp + (high + style + abs))]);//endinf index
    
                    if (ei3 < pend) {
                        pend = ei3;
                        pst = si3;
                    } else if (ei3 == pend) {
                        subvar = switchspans(size, sstyle, shigh, sunder, sp, high, under, abs, style);//for getting the right index of the span
    
                        totallength = totallength - 1;
                    } else if (pend == -1) {
                        pst = si3;
                        pend = ei3;
                    }
                }
    
                if (spannedsize2.length > sstyle + (sp + (high + under + abs))) {
    
    
                    si1 = m.getText().getSpanStart(spannedsize2[sstyle + (sp + (high + under + abs))]);
                    ei1 = m.getText().getSpanEnd(spannedsize2[sstyle + (sp + (high + under + abs))]);//endinf index
                    v = spannedsize2[sstyle + (sp + (high + under + abs))].getStyle();
    
                    if (ei1 < pend) {
                        pend = ei1;
                        pst = si1;
    
                    } else if (ei1 == pend) {
                        subvar = switchspans(size, sstyle, shigh, sunder, sp, high, under, abs, style);
                        totallength = totallength - 1;
    
                    } else if (pend == -1) {
                        pst = si1;
                        pend = ei1;
                    }
                }
                if (spannedsize3.length > shigh + (sp + (style + under + abs))) {
    
    
                    si2 = m.getText().getSpanStart(spannedsize3[shigh + (sp + (style + under + abs))]);
                    ei2 = m.getText().getSpanEnd(spannedsize3[shigh + (sp + (style + under + abs))]);//endinf index
                    colororig = spannedsize3[shigh + (sp + (style + under + abs))].getBackgroundColor();
    
    
                    if (ei2 < pend) {
                        pend = ei2;
                        pst = si2;
                    } else if (ei2 == pend) {
                        subvar = switchspans(size, sstyle, shigh, sunder, sp, high, under, abs, style);
                        totallength = totallength - 1;
                    } else if (pend == -1) {
                        pst = si2;
                        pend = ei2;
                    }
                }
                // for selecting the main index, shifting and  shifting similarities
    
                if (pend == ei) {
                    if (subvar == -1) {
                        subvar = sp + (high + style + under);
                    }
                    abs--;
                }
                if (pend == ei1) {
                    if (pend != ei) {
                        if (subvar == -1) {
                            subvar = (sstyle) + (sp + (high + under + abs));
                        }
                        style--;
                    } else {//if there is a similarity
                        sstyle++;
                    }
                }
                if (pend == ei2) {
                    if (pend != ei && pend != ei1) {
                        if (subvar == -1) {
                            subvar = (shigh) + (sp + (style + under + abs));
                        }
                        high--;
                    } else {
                        shigh++;
                    }
                }
                if (pend == ei3) {
                    if (pend != ei && pend != ei1 && pend != ei2) {
                        if (subvar == -1) {
                            subvar = (sunder) + (sp + (high + style + abs));
                        }
                        under--;
                    } else {
                        sunder++;
                    }
                }
    
                if (pst < ss && ss < pend && pend < se) {
    
    
                    if (ss < ei && si < ss && se > ei) {//abs left
    
    
                        selectionl = new SpannableString(m.getText().subSequence(si, ss));
                        selectionl.removeSpan(spannedsize[subvar]);
                        separation = "l";
                        selectionl.setSpan(new AbsoluteSizeSpan(CurrentSize), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    
                        pst = ss;
                        si = pst;
    
                    }
                    if (ss < ei1 && si1 < ss && se > ei1) {//STYLE left
                        if (!separation.contains("l")) {
                            selectionl = new SpannableString(m.getText().subSequence(si1, ss));
                            selectionl.removeSpan(spannedsize2[subvar]);
                        }
                        separation = "l";
                        if (v == Typeface.ITALIC) {//fot itallic
                            selectionl.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else if (v == Typeface.BOLD_ITALIC) {
                            selectionl.setSpan(new android.text.style.StyleSpan(Typeface.BOLD_ITALIC), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else
                            selectionl.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    
                        pst = ss;
                        si1 = pst;
                    }
                    if (ss < ei2 && si2 < ss && se > ei2) {// highlight left
    
                        if (!separation.contains("l")) {
                            selectionl = new SpannableString(m.getText().subSequence(si2, ss));
                            selectionl.removeSpan(spannedsize3[subvar]);
                        }
                        separation = "l";
    
                        selectionl.setSpan((new BackgroundColorSpan(colororig)), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        pst = ss;
                        si2 = pst;
                    }
                    if (ss < ei3 && si3 < ss && se > ei3) {// underline left
    
                        if (!separation.contains("l")) {
                            selectionl = new SpannableString(m.getText().subSequence(si3, ss));
                            selectionl.removeSpan(spannedsize4[subvar]);
                        }
                        separation = "l";
    
                        selectionl.setSpan((new UnderlineSpan()), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    
                        pst = ss;
                        si3 = pst;
                    }
                } else if (se < pend && pst < se && ss < pst) {
    
                    if (se < ei && si < se && ss < si) {//abs right
                        selectionr = new SpannableString(m.getText().subSequence(si, se));
                        selectionr.removeSpan(spannedsize[subvar]);
                        separation = "r";
                        selectionr.setSpan(new AbsoluteSizeSpan(CurrentSize), 0, selectionr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    
                        pend = se;
                        ei = pend;
                    }
                    if (se < ei1 && si1 < se && ss < si1) {//STYLE right
                        if (!separation.contains("r")) {
                            selectionr = new SpannableString(m.getText().subSequence(si1, se));
                            selectionr.removeSpan(spannedsize2[subvar]);
                        }
                        separation = "r";
                        if (v == Typeface.ITALIC) {//fot itallic
                            selectionr.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), 0, selectionr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else if (v == Typeface.BOLD_ITALIC) {
                            selectionr.setSpan(new android.text.style.StyleSpan(Typeface.BOLD_ITALIC), 0, selectionr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else
                            selectionr.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, selectionr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    
                        pend = se;
                        ei1 = pend;
                    }
                    if (se < ei2 && si2 < se && ss < si2) {// highlight right
                        if (!separation.contains("r")) {
                            selectionr = new SpannableString(m.getText().subSequence(si2, se));
                            selectionr.removeSpan(spannedsize3[subvar]);
                        }
                        separation = "r";
    
                        selectionr.setSpan((new BackgroundColorSpan(colororig)), 0, selectionr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    
                        pend = se;
                        ei2 = pend;
                        Toast.makeText(this, String.valueOf(selectionr), Toast.LENGTH_SHORT).show();
    
                    }
                    if (se < ei3 && si3 < se && ss < si3) {// underline right
    
                        if (!separation.contains("r")) {
                            selectionr = new SpannableString(m.getText().subSequence(si3, se));
                            selectionr.removeSpan(spannedsize4[subvar]);
                        }
                        separation = "r";
    
                        selectionr.setSpan((new UnderlineSpan()), 0, selectionr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    
                        pend = se;
                        ei3 = pend;
                        Toast.makeText(this, String.valueOf(selectionr), Toast.LENGTH_SHORT).show();
    
                    }
    
                }
                //separation implementation
    
                if (separation.equals("l") && sp == 0 || separation.equals("r")) {
    
                    a = (SpannedString) TextUtils.concat(a.subSequence(0, ss - selectionl.length()), selectionl, a.subSequence(ss, selectionr.toString().isEmpty() ? m.length() : se - selectionr.length()), selectionr, selectionr.toString().isEmpty() ? "" : a.subSequence(se, m.length()));
    
                    m.setText(a);
                    //separated so they can only come in right
    
                }
    
    
                if ((ss > si && se <= ei || ss >= si && se < ei) || (ss > si3 && se <= ei3 || ss >= si3 && se < ei3) || ((ss > si1 && se <= ei1 || ss >= si1 && se < ei1)) || ((ss > si2 && se <= ei2 || ss >= si2 && se < ei2))) {//inword
    
    
                    separation = "w";
    
                    if ((ss > si && se <= ei || ss >= si && se < ei)) {
    
                        selectionl = new SpannableString(m.getText().subSequence(si, ss));
                        selectionl.removeSpan(spannedsize[subvar]);
    
                        if (si != ss) {
    
                            selectionl.setSpan(new AbsoluteSizeSpan(CurrentSize), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        if (ei != se) {
                            selection.removeSpan(spannedsize[subvar]);
                            selection.setSpan(new AbsoluteSizeSpan(CurrentSize), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//to be done anyways
                        }
                        a = (SpannedString) TextUtils.concat(m.getText().subSequence(0, si), selectionl, selection, m.getText().subSequence(se, m.length()));
    
                        if (ss > si) {//shifting indexis
                            pst = ss;
                        }
                        if (se < ei) {
                            pend = se;
                            ei = pend;
                        }
    
                    }
                    if ((ss > si1 && se <= ei1 || ss >= si1 && se < ei1)) {//styling
    
    
                        if (selectionl.toString().isEmpty()) {
                            selectionl = new SpannableString(m.getText().subSequence(si1, ss));
                        } else
                            selectionl.removeSpan(spannedsize2[subvar]);
    
                        if (si1 != ss) {
                            if (v == Typeface.ITALIC) {
                                selectionl.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            } else if (v == Typeface.BOLD) {
                                selectionl.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            } else
                                selectionl.setSpan(new android.text.style.StyleSpan(Typeface.BOLD_ITALIC), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        if (ei1 != se) {
                            selection.removeSpan(spannedsize2[subvar]);
                            if (v == Typeface.BOLD) {
    
                                selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                            if (v == Typeface.ITALIC) {
    
                                selection.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                            if (v == Typeface.BOLD_ITALIC) {
    
                                selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD_ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }
                        a = (SpannedString) TextUtils.concat(m.getText().subSequence(0, si1), selectionl, selection, m.getText().subSequence(se, m.length()));
    
                        if (ss > si1) {
                            pst = ss;
                        }
                        if (se < ei1) {
                            pend = se;
                            ei1 = pend;
                        }
    
                    }
                    if ((ss > si2 && se <= ei2 || ss >= si2 && se < ei2)) {//highlight
    
    
                        if (selectionl.toString().isEmpty()) {
                            selectionl = new SpannableString(m.getText().subSequence(si2, ss));
                        } else
                            selectionl.removeSpan(spannedsize3[subvar]);
    
                        if (si2 != ss) {
                            selectionl.setSpan((new BackgroundColorSpan(colororig)), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        if (ei2 != se) {
                            selection.removeSpan(spannedsize3[subvar]);
                            selection.setSpan((new BackgroundColorSpan(colororig)), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        a = (SpannedString) TextUtils.concat(m.getText().subSequence(0, si2), selectionl, selection, m.getText().subSequence(se, m.length()));
    
                        if (ss > si2) {
                            pst = ss;
                        }
                        if (se < ei2) {
                            pend = se;
                            ei2 = pend;
                        }
                    }
                    if ((ss > si3 && se <= ei3 || ss >= si3 && se < ei3)) {//underline
    
    
                        if (selectionl.toString().isEmpty()) {
                            selectionl = new SpannableString(m.getText().subSequence(si3, ss));
                        } else
                            selectionl.removeSpan(spannedsize4[subvar]);
    
                        if (si3 != ss) {
                            selectionl.setSpan((new UnderlineSpan()), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
    
                        if (ei3 != se) {
                            selection.removeSpan(spannedsize4[subvar]);
                            selection.setSpan((new UnderlineSpan()), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        a = (SpannedString) TextUtils.concat(m.getText().subSequence(0, si3), selectionl, selection, m.getText().subSequence(se, m.length()));
    
                        if (ss > si3) {
                            pst = ss;
                        }
                        if (se < ei3) {
                            pend = se;
                            ei3 = pend;
                        }
                    }
    
    
                    m.setText(a);
    
    
                }
    
                if (separation.equals("r") || separation.equals("w")) {//resettinf for word and right part
                    spannedsize4 = m.getText().getSpans(ss, se, UnderlineSpan.class);//abs size spans selection
                    spannedsize3 = m.getText().getSpans(ss, se, BackgroundColorSpan.class);//abs size spans selection
                    spannedsize2 = m.getText().getSpans(ss, se, StyleSpan.class);//abs size spans selection
                    spannedsize = m.getText().getSpans(ss, se, AbsoluteSizeSpan.class);//abs size spans selection
                    if (separation.equals("r")) {//for addingg +ve or -ve
                        int newtotallength = spannedsize.length + spannedsize2.length + spannedsize3.length + spannedsize4.length;
                        if (actualtotallength != newtotallength) {
                            subvar = subvar - (actualtotallength - newtotallength);
                        }
                    }
                }
                if ((size == 0 || size > 0) && pend == ei || (size == -4 || size == -1) && pend == ei1 || size == -2 && pend == ei2 || size == -3 && pend == ei3) {// if span is same type
    
    
                    m.getText().removeSpan(size == -2 && ((maindiff == (se - ss) || colorchang == 1)) ? spannedsize3[subvar] : size == -2 ? null : size == -3 && maindiff == (se - ss) ? spannedsize4[subvar] : size < 0 && (maindiff == (se - ss) || ind == -2) ? spannedsize2[subvar] : size < 0 ? null : spannedsize[subvar]);//for all possabilities plus inf whole span is selected for style spans it gets removed
    
    
                }
    
                if (oldei >= 0 && pst - oldei > 1) {//get middle//
                    if (size == 0) {// FOR middle empty
                        CurrentSize = (int) (m.getTextSize() + var);// input size
                    }
                    selection = new SpannableString(m.getText().subSequence(oldei + 1, pst));
                    selection = empty(size, selection);
                    newparts = (SpannedString) TextUtils.concat(newparts, selection);
                }
                oldei = pend - 1;
    
                selection = new SpannableString(m.getText().subSequence(pst, pend));
    
                if ((ind > -1 || (size == -3 ? pend != ei3 : size == -2 ? pend != ei2 : size < 0 && pend != ei1))) {//if no spans in same type
    
    
                    if (ind > -1) {
                        selection.removeSpan(spannedsize2[subvar]);//remove the prev abs size span
                        m.getText().removeSpan(spannedsize2[subvar]);
                        selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD_ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (size == -1) {
    
                        selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    
    
                    } else if (size == -4) {
    
    
                        selection.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (size == -3) {//underlinspan
                        selection.setSpan((new UnderlineSpan()), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (size == -2) {//underlinspan
                        selection.setSpan((new BackgroundColorSpan(hicolor)), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
    
    
                } else if ((size == -4 || size == -1) && (ind == -2 || maindiff == (se - ss) && spannedsize2[subvar].getStyle() == Typeface.BOLD_ITALIC)) {
                    if (size == -1 && spannedsize2[subvar].getStyle() == Typeface.BOLD_ITALIC) {//bold
                        if (ind == -2) {
    
                            selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD_ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else
                            selection.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (size == -4 && spannedsize2[subvar].getStyle() == Typeface.BOLD_ITALIC) {//bold
                        if (ind == -2) {
                            selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD_ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else
                            selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if ((size == -1 && spannedsize2[subvar].getStyle() == Typeface.ITALIC) || (size == -4 && spannedsize2[subvar].getStyle() == Typeface.BOLD)) {//bold
    
                        selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD_ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if ((size == -1 && spannedsize2[subvar].getStyle() == Typeface.BOLD && ind == -2)) {
                        selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if ((size == -4 && spannedsize2[subvar].getStyle() == Typeface.ITALIC && ind == -2)) {
                        selection.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } else if (size == -2 && colorchang == 1) {//SPANS HIGHLIGHT AND NON HIGHLIGHT SPANS
                    selection.setSpan((new BackgroundColorSpan(hicolor)), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (size == 0 || size > 0) {//SPANS ABS SIZE,TOTAL SIZE AND NO SIZE SPANS
                    switch (size) {
                        case 0:
                            if (pend == ei) {
                                if (spannedsize[subvar].getSize() + var < 0) {
                                    CurrentSize = 1;
                                } else
                                    CurrentSize = spannedsize[subvar].getSize() + var;//adding or sub
                            } else if (m.getTextSize() + var < 0) {
                                CurrentSize = 1;
                            } else
                                CurrentSize = (int) (m.getTextSize() + var);// input size
                            break;
                        default:
                            CurrentSize = size;
    
                            break;
                    }
                    selection.setSpan(new AbsoluteSizeSpan(CurrentSize), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
    
                if (size == 0) {// FOR middle empty
                    CurrentSize = (int) (m.getTextSize() + var);// input size
                }
    
    
                newparts = (SpannedString) TextUtils.concat(newparts, selection);
    
    
                if (sp == 0 && pst > 0 && ss < pst) {
                    selection = new SpannableString(m.getText().subSequence(ss, pst));
    
                    selection = empty(size, selection);
                    newparts = (SpannedString) TextUtils.concat(selection, newparts);
                }
                if (sp == totallength - 1 && se > pend) {
    
                    selection = new SpannableString(m.getText().subSequence(pend, se));
                    pend = se;//to catch last char
                    selection = empty(size, selection);
                    newparts = (SpannedString) TextUtils.concat(newparts, selection);
                }
    
    
                a = (SpannedString) TextUtils.concat(m.getText().subSequence(0, ss), newparts, m.getText().subSequence(pend, m.length())
                );
    
    
            }
            if (totallength == 0) {
                if (size == -1) {
    
                    selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (size == -4) {
    
                    selection.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (size == -3) {//underlinspan
                    selection.setSpan((new UnderlineSpan()), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (size == -2) {
                    selection.setSpan((new BackgroundColorSpan(hicolor)), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    if (size == 0) {
                        if (m.getTextSize() + var < 0) {
                            CurrentSize = 1;
                        } else
                            CurrentSize = (int) (m.getTextSize() + var);
                    } else
                        CurrentSize = size;
    
    
                    selection.setSpan(new AbsoluteSizeSpan(CurrentSize), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
    
                a = (SpannedString) TextUtils.concat(m.getText().subSequence(0, ss), selection, m.getText().subSequence(se, m.length()));
            }
    
            m.setText(a);
    
            m.setSelection(st, end);
    
        }
```
 
As complicated as it seems its actually extremely fast because it takes all  different types of spans  
simulatinusly in one loop .
It deals with a major problem in spanning text in android and thats when part
of a spanned text is spanned the texts original 
span gets removed.
    
it also comes with these 2 methods 

  ```java
     private SpannableString empty(int size, SpannableString selection) {//for empty parts
        switch (size) {
            case -1:
                selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case -3:
                selection.setSpan((new UnderlineSpan()), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case -2:
                selection.setSpan((new BackgroundColorSpan(hicolor)), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case -4:
                selection.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            default:
                selection.setSpan(new AbsoluteSizeSpan(CurrentSize), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        return selection;
    }

    private int switchspans(int size, int sstyle, int shigh, int sunder, int sp, int high, int under, int abs, int style) {

        int subvar;
        switch (size) {
            case -1:
            case -4:
                subvar = (sstyle) + (sp + (high + under + abs));
                break;
            case -3:
                subvar = (sunder) + (sp + (high + style + abs));
                break;
            case -2:
                subvar = (shigh) + (sp + (style + under + abs));
                break;

            default:
                subvar = sp + (high + style + under);
                break;
        }

        return subvar;
    }
    ```
 

    
    
