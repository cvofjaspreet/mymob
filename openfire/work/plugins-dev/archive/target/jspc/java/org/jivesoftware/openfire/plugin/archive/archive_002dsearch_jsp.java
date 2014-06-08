package org.jivesoftware.openfire.plugin.archive;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class archive_002dsearch_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.List _jspx_dependants;

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_fmt_message_key_nobody;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_fmt_message_key_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_fmt_message_key_nobody.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n\n\n<html>\n<head>\n    <title>\n        Search Archive\n    </title>\n    <meta name=\"pageID\" content=\"openarchive-search\"/>\n    <script type=\"text/javascript\" src=\"dwr/interface/AjaxFacade.js\"></script>\n    <script type=\"text/javascript\" src=\"dwr/engine.js\"></script>\n    <script type=\"text/javascript\" src=\"dwr/util.js\"></script>\n\n    <script type=\"text/javascript\" src=\"scripts/script.aculo.us/prototype.js\"></script>\n    <script type=\"text/javascript\" src=\"scripts/script.aculo.us/effects.js\"></script>\n    <script type=\"text/javascript\" src=\"scripts/script.aculo.us/controls.js\"></script>\n    <script type=\"text/javascript\" src=\"scripts/autocomplete.js\"></script>\n\n    <script type=\"text/javascript\">\n\n    // the callback for the auto completer\n    function populateParticipant(autocompleter, token)\n    {\n        AjaxFacade.suggestParticipant(token, function(suggestions)\n        {\n            autocompleter.setChoices(suggestions)\n        });\n    }\n\n    function createAutoCompleter()\n    {\n        new Autocompleter.DWR(\"p1\", \"p1_suggestions\", populateParticipant, {})\n");
      out.write("        new Autocompleter.DWR(\"p2\", \"p2_suggestions\", populateParticipant, {})\n    }\n\n    dwr.util.setEscapeHtml(false)\n\n    function findConversations()\n    {\n        var participants = [ dwr.util.getValue('p1'), dwr.util.getValue('p2') ]\n        var startDate = dwr.util.getValue('startDate')\n        var endDate = dwr.util.getValue('endDate')\n        var dateRange = dwr.util.getValue('dateRange')\n        var keywords = dwr.util.getValue('keywords')\n\n        selectedConversation = null;\n        $('conversationLog').style.display = 'none';\n        $('conversationLogBlank').style.display = '';\n        dwr.util.removeAllRows('conversationTableBody')\n\n        AjaxFacade.findConversations(participants, startDate, endDate, dateRange, keywords, findConversationsCB)\n    }\n\n    var selectedConversation;\n    var cellFuncs = [\n            function(data)\n            {\n                return data.ownerWith\n            },\n            function(data)\n            {\n                return data.shortDate\n            },\n            function(data)\n");
      out.write("            {\n                return data.duration\n            }\n            ];\n\n    function findConversationsCB(data)\n    {\n        dwr.util.addRows('conversationTableBody', data, cellFuncs, {\n            rowCreator:function(options)\n            {\n                var tr = document.createElement(\"tr\")\n                var conversationId = options.rowData.conversationId\n                tr.id = 'conversation-' + conversationId;\n                tr.className = 'row' + (options.rowNum % 2);\n                tr.onmouseover = function()\n                {\n                    if (selectedConversation != null && this.id == 'conversation-' + selectedConversation.conversationId)\n                    {\n                        return;\n                    }\n                    this.style.backgroundColor = '#ffffee';\n                }\n                tr.onmouseout = function()\n                {\n                    if (selectedConversation != null && this.id == 'conversation-' + selectedConversation.conversationId)\n                    {\n");
      out.write("                        return;\n                    }\n                    this.style.backgroundColor = '';\n                }\n                tr.onclick = function()\n                {\n                    showConversation(conversationId)\n                }\n                return tr;\n            },\n            cellCreator:function(options)\n            {\n                var td = document.createElement(\"td\")\n                switch (options.cellNum)\n                        {\n                    case 0: td.className = \"participants\"; break;\n                    case 1: td.className = \"date\"; break;\n                    case 2: td.className = \"duration\";\n                }\n                return td;\n            }\n        })\n        $('result').style.display = '';\n    }\n\n    function showConversationCB(data)\n    {\n        if (selectedConversation != null)\n        {\n            $(\"conversation-\" + selectedConversation.conversationId).style.backgroundColor = '';\n        }\n        selectedConversation = data;\n        $('conversation-' + data.conversationId).style.backgroundColor = '#eaf1f8';\n");
      out.write("        $('conversationLogBlank').style.display = 'none';\n        $('conversationLog').style.display = '';\n        dwr.util.setValue('conversation-participants', data.participantsSingleLine);\n        dwr.util.setValue('conversation-date', data.date);\n        dwr.util.setValue('conversation-duration', data.duration);\n        dwr.util.setValue('conversationBody', data.body);\n    }\n\n    function showConversation(conversationId)\n    {\n        AjaxFacade.getConversation(conversationId, showConversationCB)\n    }\n\n    function enableDateFields()\n    {\n        dwr.util.setValue('dateRange', '');\n        return true;\n    }\n\n    function disableDateFields()\n    {\n        dwr.util.setValue('startDate', '');\n        dwr.util.setValue('endDate', '');\n        return true;\n    }\n\n    </script>\n    <style type=\"text/css\">@import url( /js/jscalendar/calendar-win2k-cold-1.css );</style>\n    <script type=\"text/javascript\" src=\"/js/jscalendar/calendar.js\"></script>\n    <script type=\"text/javascript\" src=\"/js/jscalendar/i18n.jsp\"></script>\n");
      out.write("    <script type=\"text/javascript\" src=\"/js/jscalendar/calendar-setup.js\"></script>\n    <link rel=\"stylesheet\" type=\"text/css\" href=\"styles/main.css\"/>\n</head>\n<body>\n\n<form action=\"archive-search.jsp\" method=\"post\">\n    <div class=\"jive-contentBox\">\n        <table width=\"100%\">\n            <tr valign=\"top\">\n                <td>\n                    <table>\n                        <tr>\n                            <td colspan=\"2\"><b>\n                                ");
      if (_jspx_meth_fmt_message_0(_jspx_page_context))
        return;
      out.write("\n                            </b></td>\n                        </tr>\n                        <tr valign=\"top\">\n                            <td>\n                                <input type=\"text\" id=\"p1\" name=\"p1\" size=\"30\" value=\"\"/>\n                                <div id=\"p1_suggestions\" class=\"auto_complete\"></div>\n                            </td>\n                            <td><img src=\"images/magnifier.png\" alt=\"\" vspace=\"3\" id=\"p1Trigger\"/></td>\n                        </tr>\n                        <tr valign=\"top\">\n                            <td>\n                                <input type=\"text\" id=\"p2\" name=\"p2\" size=\"30\" value=\"\"/>\n                                <div id=\"p2_suggestions\" class=\"auto_complete\"></div>\n                            </td>\n                            <td><img src=\"images/magnifier.png\" alt=\"\" vspace=\"3\" id=\"p2Trigger\"/></td>\n                        </tr>\n                    </table>\n                </td>\n                <td>\n                    <table>\n                        <tr>\n");
      out.write("                            <td colspan=\"4\"><b>\n                                ");
      if (_jspx_meth_fmt_message_1(_jspx_page_context))
        return;
      out.write("\n                            </b></td>\n                        </tr>\n                        <tr valign=\"top\">\n                            <td>\n                                <label for=\"startDate\">");
      if (_jspx_meth_fmt_message_2(_jspx_page_context))
        return;
      out.write("</label>\n                            </td>\n                            <td>\n                                <input type=\"text\" id=\"startDate\" name=\"startDate\" size=\"10\" value=\"\" onfocus=\"return enableDateFields()\"/><br/>\n                                <span class=\"jive-description\">");
      if (_jspx_meth_fmt_message_3(_jspx_page_context))
        return;
      out.write("</span>\n                            </td>\n                            <td><img src=\"images/calendar.png\" alt=\"\" vspace=\"3\" id=\"startDateTrigger\"/></td>\n                            <td rowspan=\"2\">\n                                <input type=\"radio\" name=\"dateRange\" value=\"lastDay\" id=\"lastDay\" checked=\"checked\" onclick=\"return disableDateFields()\"/>\n                                <label for=\"lastDay\">");
      if (_jspx_meth_fmt_message_4(_jspx_page_context))
        return;
      out.write("</label>\n                                <br/>\n                                <input type=\"radio\" name=\"dateRange\" value=\"lastWeek\" id=\"lastWeek\" onclick=\"return disableDateFields()\"/>\n                                <label for=\"lastWeek\">");
      if (_jspx_meth_fmt_message_5(_jspx_page_context))
        return;
      out.write("</label>\n                                <br/>\n                                <input type=\"radio\" name=\"dateRange\" value=\"lastMonth\" id=\"lastMonth\" onclick=\"return disableDateFields()\"/>\n                                <label for=\"lastMonth\">");
      if (_jspx_meth_fmt_message_6(_jspx_page_context))
        return;
      out.write("</label>\n                                <br/>\n                                <input type=\"radio\" name=\"dateRange\" value=\"lastYear\" id=\"lastYear\" onclick=\"return disableDateFields()\"/>\n                                <label for=\"lastYear\">");
      if (_jspx_meth_fmt_message_7(_jspx_page_context))
        return;
      out.write("</label>\n                            </td>\n                        </tr>\n                        <tr valign=\"top\">\n                            <td>\n                                <label for=\"endDate\">");
      if (_jspx_meth_fmt_message_8(_jspx_page_context))
        return;
      out.write("</label>\n                            </td>\n                            <td>\n                                <input type=\"text\" id=\"endDate\" name=\"endDate\" size=\"10\" value=\"\" onfocus=\"return enableDateFields()\"/><br/>\n                                <span class=\"jive-description\">");
      if (_jspx_meth_fmt_message_9(_jspx_page_context))
        return;
      out.write("</span>\n                            </td>\n                            <td><img src=\"images/calendar.png\" alt=\"\" vspace=\"3\" id=\"endDateTrigger\"/></td>\n                        </tr>\n                    </table>\n                </td>\n                <td>\n                    <table>\n                        <tr>\n                            <td colspan=\"1\"><b>\n                                ");
      if (_jspx_meth_fmt_message_10(_jspx_page_context))
        return;
      out.write("\n                            </b></td>\n                        </tr>\n                        <tr valign=\"top\">\n                            <td>\n                                <input type=\"text\" id=\"keywords\" name=\"keywords\" size=\"30\" value=\"\"/><br/>\n                                <span class=\"jive-description\">");
      if (_jspx_meth_fmt_message_11(_jspx_page_context))
        return;
      out.write("</span>\n                            </td>\n                        </tr>\n                        <tr valign=\"top\">\n                            <td align=\"right\">\n                                <br/>\n                                <input type=\"submit\" name=\"search\" value=\"");
      if (_jspx_meth_fmt_message_12(_jspx_page_context))
        return;
      out.write("\"\n                                       onclick=\"findConversations(); return false\"/>\n                            </td>\n                        </tr>\n                    </table>\n                </td>\n            </tr>\n        </table>\n    </div>\n</form>\n\n<table id=\"result\" style=\"display:none;\">\n    <tr>\n        <td width=\"35%\">\n            <div id=\"conversationList\">\n                <table id=\"conversationTable\">\n                    <tbody id=\"conversationTableBody\"></tbody>\n                </table>\n            </div>\n        </td>\n        <td width=\"60%\">\n            <div id=\"conversationLogBlank\">\n                <br/>\n                <p style=\"text-align:center;\">");
      if (_jspx_meth_fmt_message_13(_jspx_page_context))
        return;
      out.write("</p>\n            </div>\n            <div id=\"conversationLog\" style=\"display:none;\">\n                <div id=\"conversationHeader\">\n                    <span class=\"small-label\">");
      if (_jspx_meth_fmt_message_14(_jspx_page_context))
        return;
      out.write(":</span>\n                    <span class=\"small-text\" id=\"conversation-participants\"></span><br/>\n                    <span class=\"small-label\">");
      if (_jspx_meth_fmt_message_15(_jspx_page_context))
        return;
      out.write(":</span>\n                    <span class=\"small-text\" id=\"conversation-date\"></span><br/>\n                    <span class=\"small-label\">");
      if (_jspx_meth_fmt_message_16(_jspx_page_context))
        return;
      out.write(":</span>\n                    <span class=\"small-text\" id=\"conversation-duration\"></span>\n                </div>\n                <div id=\"conversationBody\">\n                    \n                </div>\n            </div>\n\n        </td>\n    </tr>\n</table>\n\n\n<script type=\"text/javascript\">\n\n    function checkDateRange()\n    {\n        var endDateField = $('endDate')\n        var startDateField = $('startDate')\n\n        var endDate = new Date(endDateField.value)\n        var startDate = new Date(startDateField.value)\n        if (endDate.getTime() < startDate.getTime())\n        {\n            alert(\"");
      if (_jspx_meth_fmt_message_17(_jspx_page_context))
        return;
      out.write("\")\n            startDateField.value = \"\"\n        }\n        else\n        {\n             enableDateFields()\n        }\n    }\n\n    Calendar.setup(\n    {\n        inputField  : \"startDate\",\n        ifFormat    : \"%m/%d/%y\",\n        button      : \"startDateTrigger\",\n        onUpdate    : checkDateRange\n    })\n\n    Calendar.setup(\n    {\n        inputField  : \"endDate\",\n        ifFormat    : \"%m/%d/%y\",\n        button      : \"endDateTrigger\",\n        onUpdate    : checkDateRange\n    })\n\n    createAutoCompleter()\n\n</script>\n\n\n</body>\n</html>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_fmt_message_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_0 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_0.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_0.setParent(null);
    _jspx_th_fmt_message_0.setKey("archive.search.participants");
    int _jspx_eval_fmt_message_0 = _jspx_th_fmt_message_0.doStartTag();
    if (_jspx_th_fmt_message_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_0);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_0);
    return false;
  }

  private boolean _jspx_meth_fmt_message_1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_1 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_1.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_1.setParent(null);
    _jspx_th_fmt_message_1.setKey("archive.search.dateRange");
    int _jspx_eval_fmt_message_1 = _jspx_th_fmt_message_1.doStartTag();
    if (_jspx_th_fmt_message_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_1);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_1);
    return false;
  }

  private boolean _jspx_meth_fmt_message_2(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_2 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_2.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_2.setParent(null);
    _jspx_th_fmt_message_2.setKey("archive.search.start");
    int _jspx_eval_fmt_message_2 = _jspx_th_fmt_message_2.doStartTag();
    if (_jspx_th_fmt_message_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_2);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_2);
    return false;
  }

  private boolean _jspx_meth_fmt_message_3(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_3 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_3.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_3.setParent(null);
    _jspx_th_fmt_message_3.setKey("archive.search.start.description");
    int _jspx_eval_fmt_message_3 = _jspx_th_fmt_message_3.doStartTag();
    if (_jspx_th_fmt_message_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_3);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_3);
    return false;
  }

  private boolean _jspx_meth_fmt_message_4(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_4 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_4.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_4.setParent(null);
    _jspx_th_fmt_message_4.setKey("archive.search.lastDay");
    int _jspx_eval_fmt_message_4 = _jspx_th_fmt_message_4.doStartTag();
    if (_jspx_th_fmt_message_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_4);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_4);
    return false;
  }

  private boolean _jspx_meth_fmt_message_5(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_5 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_5.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_5.setParent(null);
    _jspx_th_fmt_message_5.setKey("archive.search.lastWeek");
    int _jspx_eval_fmt_message_5 = _jspx_th_fmt_message_5.doStartTag();
    if (_jspx_th_fmt_message_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_5);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_5);
    return false;
  }

  private boolean _jspx_meth_fmt_message_6(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_6 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_6.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_6.setParent(null);
    _jspx_th_fmt_message_6.setKey("archive.search.lastMonth");
    int _jspx_eval_fmt_message_6 = _jspx_th_fmt_message_6.doStartTag();
    if (_jspx_th_fmt_message_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_6);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_6);
    return false;
  }

  private boolean _jspx_meth_fmt_message_7(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_7 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_7.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_7.setParent(null);
    _jspx_th_fmt_message_7.setKey("archive.search.lastYear");
    int _jspx_eval_fmt_message_7 = _jspx_th_fmt_message_7.doStartTag();
    if (_jspx_th_fmt_message_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_7);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_7);
    return false;
  }

  private boolean _jspx_meth_fmt_message_8(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_8 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_8.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_8.setParent(null);
    _jspx_th_fmt_message_8.setKey("archive.search.end");
    int _jspx_eval_fmt_message_8 = _jspx_th_fmt_message_8.doStartTag();
    if (_jspx_th_fmt_message_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_8);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_8);
    return false;
  }

  private boolean _jspx_meth_fmt_message_9(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_9 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_9.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_9.setParent(null);
    _jspx_th_fmt_message_9.setKey("archive.search.end.description");
    int _jspx_eval_fmt_message_9 = _jspx_th_fmt_message_9.doStartTag();
    if (_jspx_th_fmt_message_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_9);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_9);
    return false;
  }

  private boolean _jspx_meth_fmt_message_10(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_10 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_10.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_10.setParent(null);
    _jspx_th_fmt_message_10.setKey("archive.search.keywords");
    int _jspx_eval_fmt_message_10 = _jspx_th_fmt_message_10.doStartTag();
    if (_jspx_th_fmt_message_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_10);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_10);
    return false;
  }

  private boolean _jspx_meth_fmt_message_11(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_11 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_11.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_11.setParent(null);
    _jspx_th_fmt_message_11.setKey("archive.search.keywords.description");
    int _jspx_eval_fmt_message_11 = _jspx_th_fmt_message_11.doStartTag();
    if (_jspx_th_fmt_message_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_11);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_11);
    return false;
  }

  private boolean _jspx_meth_fmt_message_12(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_12 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_12.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_12.setParent(null);
    _jspx_th_fmt_message_12.setKey("archive.search.submit");
    int _jspx_eval_fmt_message_12 = _jspx_th_fmt_message_12.doStartTag();
    if (_jspx_th_fmt_message_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_12);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_12);
    return false;
  }

  private boolean _jspx_meth_fmt_message_13(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_13 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_13.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_13.setParent(null);
    _jspx_th_fmt_message_13.setKey("archive.search.selectConversation");
    int _jspx_eval_fmt_message_13 = _jspx_th_fmt_message_13.doStartTag();
    if (_jspx_th_fmt_message_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_13);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_13);
    return false;
  }

  private boolean _jspx_meth_fmt_message_14(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_14 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_14.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_14.setParent(null);
    _jspx_th_fmt_message_14.setKey("archive.search.participants");
    int _jspx_eval_fmt_message_14 = _jspx_th_fmt_message_14.doStartTag();
    if (_jspx_th_fmt_message_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_14);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_14);
    return false;
  }

  private boolean _jspx_meth_fmt_message_15(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_15 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_15.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_15.setParent(null);
    _jspx_th_fmt_message_15.setKey("archive.search.date");
    int _jspx_eval_fmt_message_15 = _jspx_th_fmt_message_15.doStartTag();
    if (_jspx_th_fmt_message_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_15);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_15);
    return false;
  }

  private boolean _jspx_meth_fmt_message_16(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_16 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_16.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_16.setParent(null);
    _jspx_th_fmt_message_16.setKey("archive.search.duration");
    int _jspx_eval_fmt_message_16 = _jspx_th_fmt_message_16.doStartTag();
    if (_jspx_th_fmt_message_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_16);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_16);
    return false;
  }

  private boolean _jspx_meth_fmt_message_17(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_17 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_17.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_17.setParent(null);
    _jspx_th_fmt_message_17.setKey("archive.search.error.endBeforeStart");
    int _jspx_eval_fmt_message_17 = _jspx_th_fmt_message_17.doStartTag();
    if (_jspx_th_fmt_message_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_17);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_17);
    return false;
  }
}
