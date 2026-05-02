package com.example.bursaryapp;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class BursaryData {

    public static void addBursaries() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Object[][] bursaries = {
                {"1", "NSFAS Bursary", "Any Field", 0, 350000,
                        "Full Cost of Study", "2026-11-30",
                        "NSFAS provides financial aid to eligible South African students at public universities and TVET colleges. Covers tuition, accommodation, meals, transport and personal care allowance.",
                        "https://www.nsfas.org.za/content/apply.html", "Open"},

                {"2", "Sasol Bursary", "Chemical Engineering,Mechanical Engineering,Electrical Engineering,Civil Engineering,Computer Science,IT", 65, 500000,
                        "Full Cost + Allowances", "2026-08-31",
                        "Sasol offers bursaries to students studying engineering and technology. Covers tuition, accommodation, meals and book allowance. Vacation work included.",
                        "https://www.sasol.com/careers/bursaries", "Open"},

                {"3", "Eskom Bursary", "Electrical Engineering,Mechanical Engineering,Civil Engineering,Chemical Engineering,Industrial Engineering", 65, 500000,
                        "Full Cost of Study", "2026-09-30",
                        "Eskom provides bursaries to engineering students. Includes mentorship, vacation work and possible employment after graduation.",
                        "https://www.eskom.co.za/careers", "Open"},

                {"4", "Anglo American Bursary", "Mining Engineering,Geology,Metallurgy,Chemical Engineering,Finance,Accounting", 70, 600000,
                        "Full Cost + Monthly Allowance", "2026-07-31",
                        "Anglo American provides bursaries in mining, engineering and business. Includes vacation work at Anglo American operations.",
                        "https://www.angloamerican.com/careers", "Closing Soon"},

                {"5", "Investec Bursary", "Commerce,Finance,Accounting,Economics,Actuarial Science,Mathematics", 80, 600000,
                        "Full Cost + R5,000/month", "2026-06-30",
                        "Investec offers bursaries to exceptional students in commerce and finance. Full tuition, accommodation and monthly stipend. Highly competitive.",
                        "https://www.investec.com/en_za/careers/bursaries.html", "Closing Soon"},

                {"6", "Standard Bank Bursary", "Commerce,Finance,Accounting,IT,Engineering,Statistics,Actuarial Science", 70, 500000,
                        "Full Tuition + Allowance", "2026-09-30",
                        "Standard Bank provides bursaries to students with outstanding academic ability. Includes tuition, accommodation, books and monthly allowance.",
                        "https://www.standardbank.co.za/careers", "Open"},

                {"7", "FNB Bursary", "IT,Computer Science,Software Engineering,Finance,Accounting,Data Science", 70, 500000,
                        "Full Cost + Monthly Stipend", "2026-08-31",
                        "FNB offers bursaries to students in technology and finance. Includes vacation work and possible employment after graduation.",
                        "https://www.fnb.co.za/careers", "Open"},

                {"8", "Absa Bank Bursary", "Finance,Accounting,IT,Engineering,Statistics,Commerce", 65, 500000,
                        "Full Tuition + R4,000/month", "2026-10-31",
                        "Absa provides bursaries to deserving students in finance and technology. Covers tuition, books and monthly living allowance.",
                        "https://www.absa.africa/careers", "Open"},

                {"9", "Transnet Bursary", "Engineering,Logistics,Supply Chain,IT,Finance,Accounting", 60, 400000,
                        "Full Cost of Study", "2026-08-31",
                        "Transnet offers bursaries in engineering, logistics and business. Vacation work at Transnet and possible employment after graduation.",
                        "https://www.transnet.net/careers", "Open"},

                {"10", "MTN Bursary", "IT,Computer Science,Electrical Engineering,Telecommunications,Data Science", 65, 500000,
                        "Full Tuition + Allowance + Laptop", "2026-09-30",
                        "MTN provides bursaries to technology students. Includes tuition, accommodation, monthly allowance and a laptop.",
                        "https://www.mtn.co.za/careers", "Open"},

                {"11", "Nedbank Bursary", "Finance,Accounting,IT,Commerce,Economics,Actuarial Science", 70, 500000,
                        "Full Cost + Monthly Allowance", "2026-10-31",
                        "Nedbank offers bursaries to high-achieving students in finance and technology. Includes mentorship and vacation work.",
                        "https://www.nedbank.co.za/careers", "Open"},

                {"12", "De Beers Bursary", "Mining Engineering,Geology,Metallurgy,Chemical Engineering,Mechanical Engineering", 65, 500000,
                        "Full Cost + Living Allowance", "2026-07-31",
                        "De Beers provides bursaries in mining and engineering. Vacation work at diamond mining operations included.",
                        "https://www.debeersgroup.com/careers", "Closing Soon"},

                {"13", "Vodacom Bursary", "IT,Computer Science,Electrical Engineering,Telecommunications,Data Science", 65, 500000,
                        "Full Tuition + R5,000/month", "2026-10-31",
                        "Vodacom offers bursaries to technology students. Covers tuition, accommodation and monthly stipend of R5,000.",
                        "https://www.vodacom.co.za/careers", "Open"},

                {"14", "Department of Health Bursary", "Medicine,Nursing,Pharmacy,Physiotherapy,Dentistry,Dietetics", 60, 350000,
                        "Full Cost of Study", "2026-11-30",
                        "Department of Health funds health science students. Must commit to working in public health sector after graduation.",
                        "https://www.health.gov.za/bursaries", "Open"},

                {"15", "Department of Social Development Bursary", "Social Work,Psychology,Social Sciences,Child and Youth Care", 55, 350000,
                        "Full Cost of Study", "2026-11-30",
                        "Funds students in social work and related fields. Must work in public sector after graduation.",
                        "https://www.dsd.gov.za/bursaries", "Open"},

                {"16", "Vodacom Bursary", "IT,Computer Science,Electrical Engineering,Telecommunications", 65, 500000,
                        "Full Tuition + R5,000/month", "2026-10-31",
                        "Vodacom offers bursaries to technology and engineering students.",
                        "https://www.vodacom.co.za/careers", "Open"},

                {"17", "PPC Cement Bursary", "Civil Engineering,Chemical Engineering,Mechanical Engineering", 60, 400000,
                        "Full Cost of Study", "2026-09-30",
                        "PPC Cement funds engineering students with good academic records and financial need.",
                        "https://www.ppc.co.za/careers", "Open"},

                {"18", "Shoprite Bursary", "Commerce,Retail Management,Finance,Logistics,IT", 60, 350000,
                        "Full Tuition + Allowance", "2026-11-30",
                        "Shoprite Group offers bursaries to students in commerce and retail. Includes vacation work at Shoprite stores.",
                        "https://www.shopriteholdings.co.za/careers", "Open"},

                {"19", "Harmony Gold Bursary", "Mining Engineering,Geology,Metallurgy,Chemical Engineering", 60, 400000,
                        "Full Cost of Study", "2026-08-31",
                        "Harmony Gold funds mining and engineering students. Includes vacation work at gold mining operations.",
                        "https://www.harmony.co.za/careers", "Open"},

                {"20", "Multichoice Bursary", "IT,Computer Science,Media,Communications,Engineering", 65, 500000,
                        "Full Tuition + Allowance", "2026-09-30",
                        "MultiChoice offers bursaries to students in technology and media. Includes vacation work at DStv.",
                        "https://www.multichoice.com/careers", "Open"},

                {"21", "Woolworths Bursary", "Commerce,Finance,Retail,IT,Supply Chain", 65, 400000,
                        "Full Cost of Study", "2026-10-31",
                        "Woolworths funds students in commerce and retail. Vacation work at Woolworths stores included.",
                        "https://www.woolworths.co.za/careers", "Open"},

                {"22", "Pick n Pay Bursary", "Commerce,Finance,Retail,IT,Logistics", 60, 350000,
                        "Full Tuition + Allowance", "2026-11-30",
                        "Pick n Pay offers bursaries to students in commerce and retail management.",
                        "https://www.pnp.co.za/careers", "Open"},

                {"23", "Old Mutual Bursary", "Finance,Actuarial Science,Mathematics,Statistics,Economics", 75, 500000,
                        "Full Cost + R4,500/month", "2026-09-30",
                        "Old Mutual provides bursaries to students in finance and actuarial science. Includes mentorship and vacation work.",
                        "https://www.oldmutual.co.za/careers", "Open"},

                {"24", "Discovery Bursary", "Actuarial Science,Statistics,Mathematics,Medicine,Computer Science", 80, 600000,
                        "Full Cost + R5,000/month", "2026-08-31",
                        "Discovery offers highly competitive bursaries to exceptional students. Minimum 80% average required.",
                        "https://www.discovery.co.za/careers", "Open"},

                {"25", "Impala Platinum Bursary", "Mining Engineering,Metallurgy,Chemical Engineering,Mechanical Engineering,Electrical Engineering", 65, 450000,
                        "Full Cost of Study", "2026-08-31",
                        "Implats provides bursaries to mining and engineering students. Includes vacation work at platinum mines.",
                        "https://www.implats.co.za/careers", "Open"},

                {"26", "ArcelorMittal Bursary", "Metallurgy,Mechanical Engineering,Electrical Engineering,Chemical Engineering,Industrial Engineering", 65, 450000,
                        "Full Cost + Allowance", "2026-09-30",
                        "ArcelorMittal SA funds engineering students. Vacation work at steel manufacturing plants included.",
                        "https://www.arcelormittalsa.com/careers", "Open"},

                {"27", "Tiger Brands Bursary", "Chemical Engineering,Food Technology,Mechanical Engineering,Finance,Commerce", 65, 450000,
                        "Full Tuition + Allowance", "2026-10-31",
                        "Tiger Brands offers bursaries to engineering and commerce students. Includes vacation work at food manufacturing plants.",
                        "https://www.tigerbrands.com/careers", "Open"},

                {"28", "Sappi Bursary", "Chemical Engineering,Mechanical Engineering,Electrical Engineering,Forestry,Paper Technology", 65, 450000,
                        "Full Cost of Study", "2026-09-30",
                        "Sappi provides bursaries to engineering and forestry students. Vacation work at paper mills included.",
                        "https://www.sappi.com/careers", "Open"},

                {"29", "Cell C Bursary", "IT,Computer Science,Electrical Engineering,Telecommunications,Finance", 65, 500000,
                        "Full Tuition + Allowance", "2026-10-31",
                        "Cell C offers bursaries to technology and finance students. Includes mentorship and vacation work.",
                        "https://www.cellc.co.za/careers", "Open"},

                {"30", "Sanlam Bursary", "Finance,Actuarial Science,Mathematics,Statistics,Commerce,Accounting", 75, 500000,
                        "Full Cost + R4,000/month", "2026-09-30",
                        "Sanlam provides bursaries to students in finance and actuarial science. Mentorship and vacation work included.",
                        "https://www.sanlam.co.za/careers", "Open"}
        };

        for (Object[] b : bursaries) {
            Map<String, Object> bursary = new HashMap<>();
            bursary.put("id", b[0]);
            bursary.put("name", b[1]);
            bursary.put("field", b[2]);
            bursary.put("minAverage", b[3]);
            bursary.put("maxIncome", b[4]);
            bursary.put("amount", b[5]);
            bursary.put("deadline", b[6]);
            bursary.put("description", b[7]);
            bursary.put("applyLink", b[8]);
            bursary.put("status", b[9]);

            db.collection("bursaries")
                    .document((String) b[0])
                    .set(bursary);
        }
    }
}