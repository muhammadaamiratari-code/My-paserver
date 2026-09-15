# personal_knowledge.py
# Personal & Family Knowledge — FINAL v1.2

PERSONAL_INFORMATION={
    "name":"محمد عامر مصطفیٰ",
    "preferred_address":"عامر سر",
    "family_background":"آرائیں / مہر آرائیں",
    "country":"پاکستان",
    "birth_date":"21 جولائی 2003",
    "birth_place":"آبائی گاؤں",
    "ancestral_area":"بستی لیاقت پور، کہروڑ پکا",
    "current_work":"Agricultural Farmer",
    "occupation":"Agricultural Farmer",
}

IMMEDIATE_FAMILY={
    "father":{
        "name":"محمد ظفر",
        "occupation":"Agricultural Farmer",
    },
    "mother":{
        "name":"کوثر مائی",
        "occupation":"گھریلو خاتون",
    },
    "siblings":{
        "elder_son":"محمد عامر مصطفیٰ",
        "sisters":["رقیہ ظفر","سمیہ ظفر","کینات ظفر"],
        "brothers":["ارسلان ظفر"],
    },
    "paternal_grandfather":{
        "name":"اللہ وسایا",
        "status":"مرحوم",
        "occupation":"Agricultural Farmer",
    },
    "paternal_grandmother":{
        "name":"منظور الٰہی",
        "status":"حیات",
    },
}

# منہ بولی بہنیں
ADOPTED_SISTERS={
    "sakina":{
        "name":"سکینہ عطاریہ",
        "called_name":"سکینہ",
        "relation":"منہ بولی بہن",
        "order":"سب سے بڑی",
        "father":"محمد بلال",
        "mother":"نسیریں بی بی",
        "family_location":"گیلے وال کی بستی، نوری لال کے قریب",
        "marital_status":"شادی شدہ",
        "children":{
            "count":2,
            "daughters":["بڑی بیٹی"],
            "sons":["چھوٹا بیٹا"],
        },
    },

    "zarina":{
        "name":"زرینہ عطاریہ",
        "called_name":"زرینہ",
        "relation":"منہ بولی بہن",
        "order":"سکینہ سے چھوٹی",
        "father":"محمد بلال",
        "mother":"نسیریں بی بی",
        "family_location":"گیلے وال کی بستی، نوری لال کے قریب",
        "marital_status":"غیر شادی شدہ",
        "education":"15 جماعتیں / کلاسز، کمپیوٹر سائنس کے ساتھ",
    },

    "jaweria":{
        "name":"جاویریہ عطاریہ",
        "called_name":"جاویریہ",
        "relation":"منہ بولی بہن",
        "order":"تیسری",
        "father":"محمد بلال",
        "mother":"نسیریں بی بی",
        "family_location":"گیلے وال کی بستی، نوری لال کے قریب",
        "education":"سیکنڈ ایئر تک",
        "current_activity":"گھر کے کام میں والدہ کا ہاتھ بٹاتی ہیں",
    },

    "fatima":{
        "name":"فاطمہ عطاریہ",
        "called_name":"فاطمہ",
        "relation":"منہ بولی بہن",
        "order":"چوتھی",
        "father":"محمد بلال",
        "mother":"نسیریں بی بی",
        "family_location":"گیلے وال کی بستی، نوری لال کے قریب",
        "education":"نائنتھ کلاس",
    },
}

PATERNAL_CLOSE_FAMILY={
    "paternal_uncle_jafar":{
        "relation":"چچا",
        "name":"جعفر",
        "wife":"نصرت",
        "sons":["مدن"],
        "daughters":[],
        "children_known":True,
    },

    "paternal_uncle_muhammad_nasir":{
        "relation":"چچا",
        "name":"محمد ناصر",
        "wife":"سارہ",
        "nikah_date":"26 رمضان المبارک 2026",
    },

    "paternal_aunt_naziran":{
        "relation":"پھوپھو",
        "name":"نزیراں مائی",
        "husband":"غلام سرور",
        "children":["ندیم","وسیم","نشا","نعیم","رمشا"],
        "daughter_ramsha":{
            "name":"رمشا",
            "engagement_date":"15 جولائی 2024",
            "engagement_with":"محمد عامر مصطفیٰ",
            "relationship_to_user":"منگیتر",
        },
    },

    "paternal_aunt_hafeez":{
        "relation":"پھوپھو",
        "name":"حفیظ مائی",
        "husband":"مہر اصغر",
        "children":["ثقلین","حسنین","انعم","ارم","شفنین"],
        "address":"پل مساکوٹہ",
        "current_area":"چولستان",
        "current_area_duration":"5 سال",
        "occupation":"زراعت",
    },
}

MATERNAL_FAMILY={
    "maternal_grandfather":{
        "name":"مہر عظیم",
    },

    "maternal_grandmother":{
        "name":"عظیمہ",
    },

    "maternal_uncle_ghulam_hussain":{
        "relation":"بڑے ماموں",
        "name":"غلام حسین",
        "children":["صابر حسین","ساجد","واجد","مجاہد","جنید"],
        "sons":["صابر حسین","ساجد","واجد","مجاہد","جنید"],
        "daughters":[],
    },

    "maternal_uncle_ghulam_sarwar":{
        "relation":"ماموں",
        "name":"غلام سرور",
        "children":["ندیم","وسیم","نشا","نعیم","رمشا"],
    },

    "maternal_uncle_ghulam_yasin":{
        "relation":"ماموں",
        "name":"غلام یاسین",
        "children":["مجمل"],
        "sons":["مجمل"],
        "daughters":[],
    },

    "maternal_uncle_mukhtar":{
        "relation":"ماموں",
        "name":"مختیار",
        "children":["انفال","ہمیرا","اقصیٰ","سبحان","مسکان","سفیان"],
        "daughters":["انفال","ہمیرا","اقصیٰ","مسکان"],
        "sons":["سبحان","سفیان"],
        "child_details":{
            "انفال":{
                "father":"مختیار",
                "relation":"بیٹی",
                "education":"گاؤں کا اسکول",
            },
            "ہمیرا":{
                "biological_father":"مختیار",
                "relation":"بیٹی",
                "education":"گاؤں کا اسکول",
                "special_note":"ہمیرا مختیار کی حیاتیاتی بیٹی ہے، لیکن اعجاز کے گھرانے کے ساتھ رہتی ہے۔ اعجاز اس کے حیاتیاتی والد نہیں ہیں۔",
            },
            "اقصیٰ":{
                "father":"مختیار",
                "relation":"بیٹی",
            },
            "سبحان":{
                "father":"مختیار",
                "relation":"بیٹا",
            },
            "مسکان":{
                "father":"مختیار",
                "relation":"بیٹی",
            },
            "سفیان":{
                "father":"مختیار",
                "relation":"بیٹا",
            },
        },
    },

    "maternal_uncle_ijaz":{
        "relation":"ماموں",
        "name":"اعجاز",
        "wife":"صبیہ بی بی",
        "children":["محمد سیم"],
        "sons":["محمد سیم"],
        "daughters":[],
    },

    "maternal_uncle_fayyaz":{
        "relation":"ماموں",
        "name":"فیاض",
        "wife":"ربیعہ",
        "children":["اقرا","مصباح","کرن","ایاز","ارم","شہزادی","مریم"],
    },

    "maternal_aunt_kaneez_fatima":{
        "relation":"خالہ",
        "name":"کنیز فاطمہ",
        "children":["شمیلہ","شہیب","زہیب","علیشبہ","وارث"],
    },
}

EXTENDED_PATERNAL_FAMILY={
    "paternal_great_uncle_allah_rakha":{
        "relation":"ابو کے بڑے چچا",
        "name":"اللہ رکھا",
        "wife":"گلزار",
        "children":["ثمینہ","فاروق"],
    },

    "paternal_great_uncle_allah_jiwaya":{
        "relation":"ابو کے چچا",
        "name":"اللہ جیوایا",
        "wife":"سلدار مائی",
        "children":["اسماعیل","شہناز","جمیل","شہزاد"],
        "sons":["اسماعیل","جمیل","شہزاد"],
        "daughters":["شہناز"],
        "child_details":{
            "اسماعیل":{
                "wife":"نصرت",
                "children":["ابوبکر","دانش","حسن","احسان","آسیہ"],
            },
            "شہناز":{
                "husband":"پرویز",
                "children":["عہد پرویز","فہد پرویز","انایا فاطمہ"],
            },
            "جمیل":{
                "wife":None,
                "children":[],
            },
            "شہزاد":{
                "wife":"سیما",
                "children":["شاہزین"],
            },
        },
    },

    "paternal_great_uncle_ghafoor_ahmad":{
        "relation":"ابو کے چھوٹے چچا",
        "name":"غفور احمد",
        "children":["رافیہ","نادیہ","فرحان","شان","عرفان"],
        "marital_status":{
            "married":["رافیہ","نادیہ","فرحان","عرفان"],
            "unmarried":["شان"],
        },
    },

    "paternal_great_aunt_zaiba":{
        "relation":"ابو کی پھوپھی",
        "name":"زیبا مائی",
        "husband":"مہر اسلم",
        "children":["اجمل","کوثر","جاوید","شہناز","پرویز","امتیاز","سمیرا"],
        "child_details":{
            "پرویز":{
                "wife":"شہناز",
                "wife_relation":"اللہ جیوایا کی بیٹی",
                "children":["عہد پرویز","فہد پرویز","انایا فاطمہ"],
            },
        },
    },
}

FRIENDS={
    "current_friends":[
        {
            "name":"عمر بھائی",
            "relation":"ماما اعجاز کی بیوی صبیہ بی بی کے چھوٹے بھائی",
        },
        {
            "name":"وسیم بھائی",
            "relation":"ماما غلام سرور کا چھوٹا بیٹا",
            "sibling_context":"ندیم سے چھوٹے",
        },
        {
            "name":"ناصر چچو",
            "real_name":"محمد ناصر",
            "relation":"چچا",
        },
        {
            "name":"مظہر",
            "status":"موجودہ دوست",
        },
        {
            "name":"ایاز",
            "status":"موجودہ دوست",
            "relation":"کزن",
        },
        {
            "name":"زہیب",
            "status":"موجودہ دوست",
            "relation":"خالہ کنیز فاطمہ کا بیٹا",
        },
    ],

    "other_cousins_friendship":True,

    "former_friends":[
        {
            "name":"واجد",
            "status":"سابق دوست",
            "contact":"فی الحال رابطے میں نہیں",
        },
        {
            "name":"سلمان",
            "status":"سابق دوست",
            "family_relation":"اسلم کے بیٹے اجمل کا بڑا بیٹا",
        },
        {
            "name":"سجاد",
            "status":"سابق دوست",
        },
    ],
}

RELATIONSHIP_FACTS={
    "رمشا":{
        "paternal_aunt":"نزیراں مائی",
        "paternal_aunt_husband":"غلام سرور",
        "maternal_uncle":"غلام سرور",
        "engagement_date":"15 جولائی 2024",
        "engagement_with":"محمد عامر مصطفیٰ",
        "relationship_to_user":"منگیتر",
    },

    "انفال":{
        "biological_father":"مختیار",
        "father":"مختیار",
        "relation_to_mukhtar":"بیٹی",
        "school":"گاؤں کا اسکول",
    },

    "ہمیرا":{
        "biological_father":"مختیار",
        "father":"مختیار",
        "relation_to_mukhtar":"بیٹی",
        "school":"گاؤں کا اسکول",
        "lives_with_family_of":"اعجاز",
        "important_note":"اعجاز ہمیرا کے حیاتیاتی والد نہیں ہیں۔",
    },

    "محمد سیم":{
        "father":"اعجاز",
        "mother":"صبیہ بی بی",
    },

    "شاہزین":{
        "father":"شہزاد",
        "mother":"سیما",
        "grandfather":"اللہ جیوایا",
    },

    "عہد پرویز":{
        "father":"پرویز",
        "mother":"شہناز",
    },

    "فہد پرویز":{
        "father":"پرویز",
        "mother":"شہناز",
    },

    "انایا فاطمہ":{
        "father":"پرویز",
        "mother":"شہناز",
    },
}

PEOPLE_INDEX={
    "محمد عامر مصطفیٰ":{"type":"self"},

    "محمد ظفر":{"type":"person","relation":"father"},
    "کوثر مائی":{"type":"person","relation":"mother"},

    "رقیہ ظفر":{"type":"person","relation":"sister"},
    "سمیہ ظفر":{"type":"person","relation":"sister"},
    "ارسلان ظفر":{"type":"person","relation":"brother"},
    "کینات ظفر":{"type":"person","relation":"sister"},

    "سکینہ عطاریہ":{
        "type":"person",
        "relation":"adopted_sister",
        "called_name":"سکینہ",
    },

    "زرینہ عطاریہ":{
        "type":"person",
        "relation":"adopted_sister",
        "called_name":"زرینہ",
    },

    "جاویریہ عطاریہ":{
        "type":"person",
        "relation":"adopted_sister",
        "called_name":"جاویریہ",
    },

    "فاطمہ عطاریہ":{
        "type":"person",
        "relation":"adopted_sister",
        "called_name":"فاطمہ",
    },

    "مختیار":{"type":"person","relation":"maternal_uncle"},
    "اعجاز":{"type":"person","relation":"maternal_uncle"},
    "فیاض":{"type":"person","relation":"maternal_uncle"},
    "غلام حسین":{"type":"person","relation":"maternal_uncle"},
    "غلام سرور":{"type":"person","relation":"maternal_uncle"},
    "غلام یاسین":{"type":"person","relation":"maternal_uncle"},

    "نزیراں مائی":{"type":"person","relation":"paternal_aunt"},
    "حفیظ مائی":{"type":"person","relation":"paternal_aunt"},

    "اللہ رکھا":{
        "type":"person",
        "relation":"paternal_great_uncle",
    },

    "اللہ جیوایا":{
        "type":"person",
        "relation":"paternal_great_uncle",
    },

    "غفور احمد":{
        "type":"person",
        "relation":"paternal_great_uncle",
    },

    "زیبا مائی":{
        "type":"person",
        "relation":"paternal_great_aunt",
    },

    "عمر بھائی":{"type":"person","relation":"friend"},
    "وسیم بھائی":{"type":"person","relation":"friend"},
    "ناصر چچو":{"type":"person","relation":"friend"},
    "مظہر":{"type":"person","relation":"friend"},
    "ایاز":{"type":"person","relation":"friend"},
    "زہیب":{"type":"person","relation":"friend"},

    "واجد":{"type":"person","relation":"former_friend"},
    "سلمان":{"type":"person","relation":"former_friend"},
    "سجاد":{"type":"person","relation":"former_friend"},
}

PERSONAL_KNOWLEDGE_VERSION="1.2"

__all__=[
    "PERSONAL_INFORMATION",
    "IMMEDIATE_FAMILY",
    "ADOPTED_SISTERS",
    "PATERNAL_CLOSE_FAMILY",
    "MATERNAL_FAMILY",
    "EXTENDED_PATERNAL_FAMILY",
    "FRIENDS",
    "RELATIONSHIP_FACTS",
    "PEOPLE_INDEX",
    "PERSONAL_KNOWLEDGE_VERSION",
]
