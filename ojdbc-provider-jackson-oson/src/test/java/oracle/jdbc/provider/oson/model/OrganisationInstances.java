/*
 ** Copyright (c) 2024 Oracle and/or its affiliates.
 **
 ** The Universal Permissive License (UPL), Version 1.0
 **
 ** Subject to the condition set forth below, permission is hereby granted to any
 ** person obtaining a copy of this software, associated documentation and/or data
 ** (collectively the "Software"), free of charge and under any and all copyright
 ** rights in the Software, and any and all patent rights owned or freely
 ** licensable by each licensor hereunder covering either (i) the unmodified
 ** Software as contributed to or provided by such licensor, or (ii) the Larger
 ** Works (as defined below), to deal in both
 **
 ** (a) the Software, and
 ** (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 ** one is included with the Software (each a "Larger Work" to which the Software
 ** is contributed by such licensors),
 **
 ** without restriction, including without limitation the rights to copy, create
 ** derivative works of, display, perform, and distribute the Software and make,
 ** use, sell, offer for sale, import, export, have made, and have sold the
 ** Software and the Larger Work(s), and to sublicense the foregoing rights on
 ** either these or other terms.
 **
 ** This license is subject to the following condition:
 ** The above copyright notice and either this complete permission notice or at
 ** a minimum a reference to the UPL must be included in all copies or
 ** substantial portions of the Software.
 **
 ** THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 ** IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 ** FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 ** AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 ** LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 ** OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 ** SOFTWARE.
 */
package oracle.jdbc.provider.oson.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds and returns {@Code Organisation}
 */
public class OrganisationInstances {
    static final String[] organizations = {
            "Oracle Corporation",
            "Oracle Financial Services",
            "Oracle Cloud Infrastructure",
            "Oracle Consulting",
            "Oracle Academy",
            "Oracle Labs",
            "Oracle Hospitality",
            "Oracle NetSuite",
            "Oracle Japan",
            "Oracle University"
    };
    static List<Organisation> organisationInstances = new ArrayList<Organisation>();


    public static void buildOrganisationInstances() {
        for (int i = 0; i < 10; i++) {
            int no_of_employees = employeesCount[i];
            String organisationName = organizations[i];
            List<Employee> employees = new ArrayList<>();
            for (int j = 0; j < no_of_employees; j++) {
                employees.add(EmployeeInstances.getEmployee());
            }
            organisationInstances.add(new Organisation(organisationName, employees));
        }
    }

    static final int[] employeesCount = {100, 100, 100, 100, 100, 100, 100, 100, 100, 100};

    public static List<Organisation> getInstances() {
        if (organisationInstances.isEmpty()) {
            buildOrganisationInstances();
        }
        return organisationInstances;
    }
}
