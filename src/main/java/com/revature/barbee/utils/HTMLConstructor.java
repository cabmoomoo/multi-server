package com.revature.barbee.utils;

public class HTMLConstructor {
    
    /**
     * Takes a few strings returns a string surrounded by the boilerplate of HTML.
     * 
     * The multi-line parameters expect input to include tabs and a final newline. Web browsers do not care,
     * so it makes little functional difference.
     * 
     * @param title Single line of text for the title (browser tab name) of your page
     * @param style Multi-line css styling for your document
     * @param body Multi-line body of the document
     * @return Completed HTML document in a String
     */
    public static StringBuilder boilerplateHTML(String title, String style, StringBuilder body) {
        StringBuilder result = new StringBuilder();
        result.append( 
            """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="utf-8">
                <title>""");
        result.append(title); // Page title
        result.append( 
            """
            </title>
            <style>
            """);
        result.append(style);
        result.append( 
            """
            </style>
            </head>
            <body>
            """);

        result.append(body);
        
        result.append(
            """
            </body>
            </html>
            """);

        return result;
    }

}
