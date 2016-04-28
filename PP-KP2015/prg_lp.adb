-------------------------------
-- Lab 2
-- Ada. Win32.
-- Isaichuk Pavel Gennadievich
-- IP-z21, FIOT
-- A = max(Z)*B + alpha*C*(MO*MK)
-------------------------------
with Ada.Text_IO, Ada.Integer_Text_IO, Ada.Synchronous_Task_Control, Ada.Float_Text_IO, Ada.Calendar;
 use Ada.Text_IO, Ada.Integer_Text_IO, Ada.Synchronous_Task_Control, Ada.Float_Text_IO, Ada.Calendar;
procedure prg_lp is
   N : Integer := 100;
   P : Integer := 10;
   H : Integer := N/P;

   type Vector is array (1..N) of Integer;
   type Vector1H is array (1..H*1) of Integer;
   type Vector2H is array (1..H*2) of Integer;
   type Vector3H is array (1..H*3) of Integer;
   type Vector4H is array (1..H*4) of Integer;
   type Vector5H is array (1..H*5) of Integer;


   type Matrix is array (1..N) of Vector;
   type Matrix1H is array (1..H*1) of Vector;
   type Matrix2H is array (1..H*2) of Vector;
   type Matrix3H is array (1..H*3) of Vector;
   type Matrix4H is array (1..H*4) of Vector;
   type Matrix5H is array (1..H*5) of Vector;

   ------------------------------------------
   --                                      --
   --        HELPING FUNCTIONS             --
   --                                      --
   ------------------------------------------
   procedure vectorInput(V : in out Vector) is
    begin
       for i in 1..N loop
          V(i) := 1;
       end loop;
    end vectorInput;

   procedure vectorOutput(V : in Vector) is
      indx : integer := 1;
   begin
       for i in 1..N loop
         Put(V(i));
         if indx mod 10 = 0 then
            New_Line;
         end if;
         indx := indx + 1;
      end loop;
       New_Line;
    end vectorOutput;

   procedure matrixInput(M : in out Matrix) is
    begin
       for i in 1..N loop
          for j in 1..N loop
             M(i)(j) := 1;
          end loop;
       end loop;
    end matrixInput;

   procedure reduceVector(V : in Vector; Vr : out Vector5H) is
      indx : integer := 1;
   begin
      for i in H+1..H*6 loop
         Vr(indx) := V(i);
         indx := indx + 1;
      end loop;
   end reduceVector;

   procedure reduceMatrix(M : in Matrix; Mr : out Matrix5H) is
      indx : integer := 1;
   begin
      for i in H+1..H*6 loop
         Mr(indx) := M(i);
         indx := indx + 1;
      end loop;
   end reduceMatrix;

   procedure reduceVector(V : in Vector; Vr : out Vector4H) is
   begin
      for i in (H*6+1)..N loop
         Vr(i-H*6) := V(i);
      end loop;
   end reduceVector;

   procedure reduceMatrix(M : in Matrix; Mr : out Matrix4H) is
      indx : integer := 1;
   begin
      for i in H*6+1..N loop
         Mr(indx) := M(i);
         indx := indx + 1;
      end loop;
   end reduceMatrix;

   procedure reduceVector(V : in Vector5H; Vr : out Vector4H) is
      indx : integer := 1;
   begin
      for i in (H+1)..H*5 loop
         Vr(indx) := V(i);
         indx := indx + 1;
      end loop;
   end;

   procedure reduceMatrix(M : in Matrix5H; Mr : out Matrix4H) is
      indx : integer := 1;
   begin
      for i in H+1..H*5 loop
         Mr(indx) := M(i);
         indx := indx + 1;
      end loop;
   end;

   procedure reduceVector(V : in Vector4H; Vr : out Vector3H) is
      indx : integer := 1;
   begin
      for i in H+1..H*4 loop
         Vr(indx) := V(i);
         indx := indx + 1;
      end loop;
   end;

   procedure reduceMatrix(M : in Matrix4H; Mr : out Matrix3H) is
      indx : integer := 1;
   begin
      for i in H+1..H*4 loop
         Mr(indx) := M(i);
         indx := indx + 1;
      end loop;
   end;

   procedure reduceVector(V : in Vector3H; Vr : out Vector2H) is
      indx : integer := 1;
   begin
      for i in H+1..H*3 loop
         Vr(indx) := V(i);
         indx := indx + 1;
      end loop;
   end;

   procedure reduceMatrix(M : in Matrix3H; Mr : out Matrix2H) is
      indx : integer := 1;
   begin
      for i in H+1..H*3 loop
         Mr(indx) := M(i);
         indx := indx + 1;
      end loop;
   end;

   procedure reduceVector(V : in Vector2H; Vr : out Vector1H) is
      indx : integer := 1;
   begin
      for i in H+1..H*2 loop
         Vr(indx) := V(i);
         indx := indx + 1;
      end loop;
   end;

   procedure reduceMatrix(M : in Matrix2H; Mr : out Matrix1H) is
      indx : integer := 1;
   begin
      for i in H+1..H*2 loop
         Mr(indx) := M(i);
         indx := indx + 1;
      end loop;
   end;

   procedure getPart(V : in Vector; Vr : out Vector1H) is
   begin
      for i in 1..H loop
         Vr(i) := V(i);
      end loop;
   end;

   procedure getPart(V : in Vector2H; Vr : out Vector1H) is
   begin
      for i in 1..H loop
         Vr(i) := V(i);
      end loop;
   end;

   procedure getPart(V : in Vector3H; Vr : out Vector1H) is
   begin
      for i in 1..H loop
         Vr(i) := V(i);
      end loop;
   end;

   procedure getPart(V : in Vector4H; Vr : out Vector1H) is
   begin
      for i in 1..H loop
         Vr(i) := V(i);
      end loop;
   end;

   procedure getPart(V : in Vector5H; Vr : out Vector1H) is
   begin
      for i in 1..H loop
         Vr(i) := V(i);
      end loop;
   end;

   procedure getPart(M : in Matrix5H; Mr : out Matrix1H) is
   begin
      for i in 1..H loop
         Mr(i) := M(i);
      end loop;
   end;

   procedure getPart(M : in Matrix; Mr : out Matrix1H) is
   begin
      for i in 1..H loop
         Mr(i) := M(i);
      end loop;
   end;

   procedure getPart(M : in Matrix4H; Mr : out Matrix1H) is
   begin
      for i in 1..H loop
         Mr(i) := M(i);
      end loop;
   end;

   procedure getPart(M : in Matrix3H; Mr : out Matrix1H) is
   begin
      for i in 1..H loop
         Mr(i) := M(i);
      end loop;
   end;

   procedure getPart(M : in Matrix2H; Mr : out Matrix1H) is
   begin
      for i in 1..H loop
         Mr(i) := M(i);
      end loop;
   end;

   procedure getMax(V : in Vector1H; max : out integer) is
   begin
      max := -10000;
      for i in 1..H loop
         if V(i) > max then
            max := V(i);
         end if;
      end loop;
   end;

   procedure countA(max : in Integer; B : in Vector1H; alpha : in integer;
                   C : in Vector; MO : in Matrix; MK : in Matrix1H; A : out Vector1H) is
      CH, MOK : Integer;
      S: Vector;
   begin
      for i in 1..H loop
            CH := 0;
            S := MK(i);
            for x in 1..N loop
               MOK := 0;
               for y in 1..N loop
                  MOK := MOK + MO(x)(y)*S(y);
               end loop;
               CH := CH + C(x) * MOK;
            end loop;
            A(i) := max * B(i) + alpha * CH;
         end loop;
   end countA;

   ------------------------------------------
   --                                      --
   --               TASKS                  --
   --                                      --
   ------------------------------------------
   procedure tasks is
      task T1 is
         entry Data2(B1 : in Vector1H; alpha : integer; MK1 : in Matrix1H);
      end T1;
      task T2 is
         entry Z(Z4 : in Vector4H);
         entry Max(max : in out integer);
         entry Data(MO : in Matrix; C : in Vector; max : in integer);
         entry Result(A4 : out Vector4H);
         entry Data2(B2 : in Vector2H; alpha : integer; MK2 : in Matrix2H);
      end T2;
      task T3 is
         entry Z(Z3 : in Vector3H);
         entry Max(max : in out integer);
         entry Data(MO : in Matrix; C : in Vector; max : in integer);
         entry Result(A3 : out Vector3H);
         entry Data2(B3 : in Vector3H; alpha : integer; MK3 : in Matrix3H);
      end T3;
      task T4 is
         entry Z(Z2 : in Vector2H);
         entry Max(max : in out integer);
         entry Data(MO : in Matrix; C : in Vector; max : in integer);
         entry Result(A2 : out Vector2H);
         entry Data2(B4 : in Vector4H; alpha : integer; MK4 : in Matrix4H);
      end T4;
      task T5 is
         entry Z(Z1 : in Vector1H);
         entry Max(max : in out integer);
         entry Data(MO : in Matrix; C : in Vector; max : in integer);
         entry Result(A1 : out Vector1H);
         entry Data2(B5 : in Vector5H; alpha : integer; MK5 : in Matrix5H);
      end T5;
      task T6 is
         entry Z(Z5 : in Vector5H);
         entry Max(max : in out integer);
         entry Data(MO : in Matrix; C : in Vector; max : in integer);
         entry Result(A5 : out Vector5H);
         entry Data2(B1 : in Vector1H; alpha : integer; MK1 : in Matrix1H);
      end T6;
      task T7 is
         entry Z(Z4 : in Vector4H);
         entry Max(max : in out integer);
         entry Data(MO : in Matrix; C : in Vector; max : in integer);
         entry Result(A4 : out Vector4H);
         entry Data2(B2 : in Vector2H; alpha : integer; MK2 : in Matrix2H);
      end T7;
      task T8 is
         entry Z(Z3 : in Vector3H);
         entry Max(max : in out integer);
         entry Data(MO : in Matrix; C : in Vector; max : in integer);
         entry Result(A3 : out Vector3H);
         entry Data2(B3 : in Vector3H; alpha : integer; MK3 : in Matrix3H);
      end T8;
      task T9 is
         entry Z(Z2 : in Vector2H);
         entry Max(max : in out integer);
         entry Data(MO : in Matrix; C : in Vector; max : in integer);
         entry Result(A2 : out Vector2H);
         entry Data2(B4 : in Vector4H; alpha : integer; MK4 : in Matrix4H);
      end T9;
      task T10 is
         entry Z(Z1 : in Vector1H);
         entry Max(max : in out integer);
         entry Data(MO : in Matrix; C : in Vector; max : in integer);
         entry Result(A1 : out Vector1H);
      end T10;

      task body T1 is
         A, Z, C : Vector;
         Z4 : Vector4H;
         Z5 : Vector5H;
         Z1, B, A1 : Vector1H;
         m, alpha1: integer;
         MO : Matrix;
         A4 : Vector4H;
         A5 : Vector5H;
         MK : Matrix1H;
      begin
         Put_Line("T1 started!");
         --input
         vectorInput(Z);
         vectorInput(C);
         matrixInput(MO);
         getPart(Z, Z1);
         reduceVector(Z, Z4);
         T2.Z(Z4);
         reduceVector(Z, Z5);
         T6.Z(Z5);
         Put_Line("T1 : Z sent to T2 and T6.");

         --local max
         getMax(Z1, m);
         T2.Max(m);
         T6.Max(m);
         Put("T1 : Got max.");
         Put(m); New_Line;

         --send data
         T2.Data(MO, C, m);
         T6.Data(MO, C, m);
         Put_Line("T1 : Sent MO, C, max.");

         --receive data
         accept Data2 (B1 : in Vector1H; alpha : in integer; MK1 : in Matrix1H) do
            B := B1;
            alpha1 := alpha;
            MK := MK1;
         end Data2;
         Put_Line("T1 : Received MK, B, alpha.");

         --computations of A
         countA(m, B, alpha1, C, MO, MK, A1);

         for i in 1..H loop
            A(i) := A1(i);
         end loop;

         T2.Result(A4);
         T6.Result(A5);
         for i in 1..H*4 loop
            A(H*6+i) := A4(i);
         end loop;
         for i in 1..H*5 loop
            A(i+H) := A5(i);
         end loop;
         --computations finished
         Put_Line("Task-1 finished!");
         vectorOutput(A);
      end T1;

      task body T2 is
         m, alpha2 : integer;
         Z3 : Vector3H;
         Z1, B1, B, A1 : Vector1H;
         A : Vector4H;
         MO2 : Matrix;
         C2 : Vector;
         A3 : Vector3H;
         MK1, MK : Matrix1H;
      begin
         Put_Line("T2 started!");
         --wait for input to finish
         accept Z (Z4 : in Vector4H) do
            getPart(Z4, Z1);
            reduceVector(Z4, Z3);
         end Z;
         Put_Line("T2 : Got Z.");
         T3.Z(Z3);
         Put_Line("T2 : Z sent to T3.");

         --count local max
         getMax(Z1, m);
         T3.Max(m);
         accept Max (max : in out integer) do
            if max < m then
               max := m;
            end if;
         end Max;
         Put("T2 : Got max.");
         Put(m); New_Line;

         accept Data (MO : in Matrix; C : in Vector; max : in integer) do
            MO2 := MO;
            C2 := C;
            m := max;
            Put_Line("T2 : Received MO, C, max.");
         end Data;
         T3.Data(MO2, C2, m);


         accept Data2 (B2 : in Vector2H; alpha : in integer; MK2 : in Matrix2H) do
            getPart(B2, B);
            reduceVector(B2, B1);
            alpha2 := alpha;
            getPart(MK2, MK);
            reduceMatrix(MK2, MK1);
            Put_Line("T2 : Received MK, B, alpha.");
         end Data2;
         T1.Data2(B1, alpha2, MK1);

         --computations
         countA(m, B, alpha2, C2, MO2, MK, A1);
         for i in 1..H loop
            A(i) := A1(i);
         end loop;
         T3.Result(A3);
         for i in H+1..H*4 loop
            A(i) := A3(i-H);
         end loop;

         accept Result (A4 : out Vector4H) do
            A4 := A;
         end Result;

         --computations finished
         Put_Line("Task2 finished!");
      end T2;

      task body T3 is
         m, alpha3 : integer;
         Z1, B, A1 : Vector1H;
         Z2, B2 : Vector2H;
         A : Vector3H;
         MO3 : Matrix;
         C3 : Vector;
         A2 : Vector2H;
         MK : Matrix1H;
         MK2:Matrix2H;
      begin
         Put_Line("T3 started!");
         --wait for input to finish
         accept Z (Z3 : in Vector3H) do
            getPart(Z3, Z1);
            reduceVector(Z3, Z2);
         end Z;
         Put_Line("T3 : Got Z.");
         T4.Z(Z2);
         Put_Line("T3 : Z sent to T4.");
         --count local max
         getMax(Z1, m);
         T4.Max(m);
         accept Max (max : in out integer) do
            if max < m then
               max := m;
            end if;
         end Max;
         Put("T3 : Got max: "); Put(m); New_Line;

         --receive data
         accept Data (MO : in Matrix; C : in Vector; max : in integer) do
            MO3 := MO;
            C3 := C;
            m := max;
            Put_Line("T3 : Received MO, C, max.");
         end Data;
         T4.Data(MO3, C3, m);

         accept Data2 (B3 : in Vector3H; alpha : in integer; MK3 : in Matrix3H) do
            getPart(B3, B);
            reduceVector(B3, B2);
            alpha3 := alpha;
            getPart(MK3, MK);
            reduceMatrix(MK3, MK2);
            Put_Line("T3 : Received MK, B, alpha.");
         end Data2;
         T2.Data2(B2, alpha3, MK2);

         --computations
         countA(m, B, alpha3, C3, MO3, MK, A1);
         for i in 1..H loop
            A(i) := A1(i);
         end loop;

         T4.Result(A2);
         for i in H+1..H*3 loop
            A(i) := A2(i-H);
         end loop;

         accept Result (A3 : out Vector3H) do
            A3 := A;
         end Result;

         --computations finished
         Put_Line("Task3 finished!");
      end T3;

      task body T4 is
         m, alpha4 : integer;
         Z1, Z1_5, B, A1 : Vector1H;
         A : Vector2H;
        MO4 : Matrix;
         C4 : Vector;
         MK3: Matrix3H;
         B3 : Vector3H;
         MK : Matrix1H;
      begin
         Put_Line("T4 started!");
         --wait for input to finish
         accept Z (Z2 : in Vector2H) do
            getPart(Z2, Z1);
            reduceVector(Z2, Z1_5);
         end Z;
         Put_Line("T4 : Got Z.");
         --send Z to T5
         T5.Z(Z1_5);
         Put_Line("T4 : Z sent to T5.");
         --count local max
         getMax(Z1, m);
         T5.Max(m);
         accept Max (max : in out integer) do
            if max < m then
               max := m;
            end if;
         end Max;
         Put("T4 : Got max.");
         Put(m); New_Line;

         --receive data
         accept Data (MO : in Matrix; C : in Vector; max : in integer) do
            MO4 := MO;
            C4 := C;
            m := max;
            Put_Line("T4 : Received MO, C, max.");
         end Data;
         T5.Data(MO4, C4, m);

         accept Data2 (B4 : in Vector4H; alpha : in integer; MK4 : in Matrix4H) do
            getPart(B4, B);
            reduceVector(B4, B3);
            alpha4 := alpha;
            getPart(MK4, MK);
            reduceMatrix(MK4, MK3);
            Put_Line("T4 : Received MK, B, alpha.");
         end Data2;
         T3.Data2(B3, alpha4, MK3);

         --computations
         countA(m, B, alpha4, C4, MO4, MK, A1);
         for i in 1..H loop
            A(i) := A1(i);
         end loop;

         T5.Result(A1);
         for i in H+1..H*2 loop
            A(i) := A1(i-H);
         end loop;

         accept Result (A2 : out Vector2H) do
            A2 := A;
         end Result;

         --computations finished
         Put_Line("Task4 finished!");
      end T4;

      task body T5 is
         m, alpha5 : Integer;
         Z1_1, A, B : Vector1H;
        MO5 : Matrix;
         C5 : Vector;
         MK4: Matrix4H;
         MK : Matrix1H;
         B4 : Vector4H;
      begin
         Put_Line("T5 started!");
         accept Z (Z1 : in Vector1H) do
            Z1_1 := Z1;
         end Z;
         Put_Line("T5 : Got Z.");
         getMax(Z1_1, m);
         accept Max (max : in out integer) do
            if max < m then
               max := m;
            end if;
         end Max;
         Put("T5 : Got max.");
         Put(m); New_Line;

         accept Data (MO : in Matrix; C : in Vector; max : in integer) do
            MO5 := MO;
            C5 := C;
            m := max;
            Put_Line("T5 : Received MO, C, max.");
         end Data;

         accept Data2 (B5 : in Vector5H; alpha : in integer; MK5 : in Matrix5H) do
            getPart(B5, B);
            reduceVector(B5, B4);
            alpha5 := alpha;
            getPart(MK5, MK);
            reduceMatrix(MK5, MK4);
            Put_Line("T5 : Received MK, B, alpha.");
         end Data2;
         T4.Data2(B4, alpha5, MK4);

         --computations
         countA(m, B, alpha5, C5, MO5, MK, A);

         accept Result (A1 : out Vector1H) do
            A1 := A;
         end Result;

         --computations finished
         Put_Line("Task5 finished!");
      end T5;


      task body T6 is
         m, alpha6 : Integer;
         Z1, B, A1 : Vector1H;
         Z4 : Vector4H;
         MO6 : Matrix;
         MK : Matrix1H;
         C6 : Vector;
         A : Vector5H;
         A4: Vector4H;
      begin
         Put_Line("T6 started!");
         --wait for input to finish
         accept Z (Z5 : in Vector5H) do
            getPart(Z5, Z1);
            reduceVector(Z5, Z4);
         end Z;
         Put_Line("T6 : Got Z.");
         --send Z to T7
         T7.Z(Z4);
         Put_Line("T6 : Z sent to T7.");
         --count local max
         getMax(Z1, m);
         T7.Max(m);
         accept Max (max : in out integer) do
            if max < m then
               max := m;
            end if;
         end Max;
         Put("T6 : Got max.");
         Put(m); New_Line;

         accept Data (MO : in Matrix; C : in Vector; max : in integer) do
            MO6 := MO;
            C6 := C;
            m := max;
            Put_Line("T6 : Received MO, C, max.");
         end Data;
         T7.Data(MO6, C6, m);

         accept Data2 (B1 : in Vector1H; alpha : in integer; MK1 : in Matrix1H) do
            MK := MK1;
            alpha6 := alpha;
            B := B1;
            Put_Line("T6 : Received MK, B, alpha.");
         end Data2;

         --computations
         countA(m, B, alpha6, C6, MO6, MK, A1);
         for i in 1..H loop
            A(i) := A1(i);
         end loop;

         T7.Result(A4);
         for i in 1..H*4 loop
            A(H+i) := A4(i);
         end loop;

         accept Result (A5 : out Vector5H) do
            A5 := A;
         end Result;

         --computations finished
         Put_Line("Task6 finished!");
      end T6;

      task body T7 is
         m, alpha7 : Integer;
         Z3 : Vector3H;
         Z1, B, B1, A1 : Vector1H;
         MK1, MK : Matrix1H;
         MO7 : Matrix;
         C7 : Vector;
         A : Vector4H;
         A3 : Vector3H;
      begin
         Put_Line("T7 started!");
         --wait for input to finish
         accept Z (Z4 : in Vector4H) do
            getPart(Z4, Z1);
            reduceVector(Z4, Z3);
         end Z;
         Put_Line("T7 : Got Z.");
         --send Z to T7
         T8.Z(Z3);
         Put_Line("T7 : Z sent to T8.");
         --count local max
         getMax(Z1, m);
         T8.Max(m);
         accept Max (max : in out integer) do
            if max < m then
               max := m;
            end if;
         end Max;
         Put("T7 : Got max.");
         Put(m); New_Line;

         accept Data (MO : in Matrix; C : in Vector; max : in integer) do
            MO7 := MO;
            C7 := C;
            m := max;
            Put_Line("T7 : Received MO, C, max.");
         end Data;
         T8.Data(MO7, C7, m);

         accept Data2 (B2 : in Vector2H; alpha : in integer; MK2 : in Matrix2H) do
            getPart(MK2, MK);
            reduceMatrix(MK2, MK1);
            alpha7 := alpha;
            getPart(B2, B);
            reduceVector(B2, B1);
            Put_Line("T7 : Received MK, B, alpha.");
         end Data2;
         T6.Data2(B1, alpha7, MK1);

         --computations
         countA(m, B, alpha7, C7, MO7, MK, A1);
         for i in 1..H loop
            A(i) := A1(i);
         end loop;

         T8.Result(A3);
         for i in 1..H*3 loop
            A(H+i) := A3(i);
         end loop;

         accept Result (A4 : out Vector4H) do
            A4 := A;
         end Result;

         --computations finished
         Put_Line("Task7 finished!");
      end T7;

      task body T8 is
         m, alpha8 : Integer;
         Z1, B, A1 : Vector1H;
         Z2 : Vector2H;
         MO8 : Matrix;
         C8 : Vector;
         A2, B2 : Vector2H;
         A : Vector3H;
         MK : Matrix1H;
         MK2: Matrix2H;
      begin
         Put_Line("T8 started!");
         --wait for input to finish
         accept Z (Z3 : in Vector3H) do
            getPart(Z3, Z1);
            reduceVector(Z3, Z2);
         end Z;
         Put_Line("T8 : Got Z.");
         --send Z to T9
         T9.Z(Z2);

         --count local max
         getMax(Z1, m);
         T9.Max(m);
         accept Max (max : in out integer) do
            if max < m then
               max := m;
            end if;
         end Max;
         Put("T8 : Got max.");
         Put(m); New_Line;

         accept Data (MO : in Matrix; C : in Vector; max : in integer) do
            MO8 := MO;
            C8 := C;
            m := max;
            Put_Line("T8 : Received MO, C, max.");
         end Data;
         T9.Data(MO8, C8, m);

         accept Data2 (B3 : in Vector3H; alpha : in integer; MK3 : in Matrix3H) do
            getPart(MK3, MK);
            reduceMatrix(MK3, MK2);
            alpha8 := alpha;
            getPart(B3, B);
            reduceVector(B3, B2);
            Put_Line("T8 : Received MK, B, alpha.");
         end Data2;
         T7.Data2(B2, alpha8, MK2);

         --computations
         countA(m, B, alpha8, C8, MO8, MK, A1);
         for i in 1..H loop
            A(i) := A1(i);
         end loop;

         T9.Result(A2);
         for i in 1..H*2 loop
            A(H+i) := A2(i);
         end loop;

         accept Result (A3 : out Vector3H) do
            A3 := A;
         end Result;
         --computations finished
         Put_Line("Task8 finished!");
      end T8;

      task body T9 is
         m, alpha9 : Integer;
         Z1, Z1_10 : Vector1H;
         MO9 : Matrix;
         C9 : Vector;
         A : Vector2H;
         A1 : Vector1H;
         B : Vector1H;
         B3: Vector3H;
         MK : Matrix1H;
         MK3 : Matrix3H;
      begin
         Put_Line("T9 started!");
         --wait for input to finish
         accept Z (Z2 : in Vector2H) do
            getPart(Z2, Z1);
            reduceVector(Z2, Z1_10);
         end Z;
         Put_Line("T9 : Got Z.");

         --send Z to T10
         T10.Z(Z1_10);
         Put_Line("T9 : Z sent to T10.");
         getMax(Z1, m);
         T10.Max(m);
         accept Max (max : in out integer) do
            if max < m then
               max := m;
            end if;
         end Max;
         Put("T9 : Got max.");
         Put(m); New_Line;

         accept Data (MO : in Matrix; C : in Vector; max : in integer) do
            MO9 := MO;
            C9 := C;
            m := max;
            Put_Line("T9 : Received MO, C, max.");
         end Data;
         T10.Data(MO9, C9, m);

         --get other Data
         accept Data2 (B4 : in Vector4H; alpha : in integer; MK4 : in Matrix4H) do
            reduceVector(B4, B3);
            getPart(B4, B);
            getPart(MK4, MK);
            reduceMatrix(MK4, MK3);
            alpha9 := alpha;
            Put_Line("T9 : Received MK, B, alpha.");
         end Data2;
         T8.Data2(B3, alpha9, MK3);

         --computations
         countA(m, B, alpha9, C9, MO9, MK, A1);
         for i in 1..H loop
            A(i) := A1(i);
         end loop;

         T10.Result(A1);
         for i in 1..H loop
            A(H+i) := A1(i);
         end loop;

         accept Result (A2 : out Vector2H) do
            A2 := A;
         end Result;

         --computations finished
         Put_Line("Task9 finished!");
      end T9;

      task body T10 is
         B : Vector;
         B4 : Vector4H;
         B5 : Vector5H;
         alpha, m : Integer;
         MK : Matrix;
         MK4 : Matrix4H;
         MK5 : Matrix5H;
         Z1_1, A, B1 : Vector1H;
         MO10 : Matrix;
         C10 : Vector;
         MK1 : Matrix1H;

      begin
         Put_Line("T10 started!");
         alpha := 1;
         matrixInput(MK);
         vectorInput(B);
         Put_Line("T10 : Input done.");

         accept Z (Z1 : in Vector1H) do
            Z1_1 := Z1;
         end Z;
         Put_Line("T10 : Got Z.");

         --get max
         getMax(Z1_1, m);
         accept Max (max : in out integer) do
            if m > max then
               max := m;
            end if;
         end Max;
         Put("T10 : Got max.");
         Put(m); New_Line;

         --receive data fo computing
         accept Data (MO : in Matrix; C : in Vector; max : in integer) do
            MO10 := MO;
            C10 := C;
            m := max;
         end Data;
         Put_Line("T10 : Received MO, C, max.");

         --send input data to T9
         reduceVector(B, B4);
         reduceMatrix(MK, MK4);
         getPart(MK, MK1);
         T9.Data2(B4, alpha, MK4);
         Put_Line("T10 : Send MK, B, alpha to T9.");
         --send input data to T5
         reduceVector(B, B5);
         reduceMatrix(MK, MK5);
         getPart(B, B1);
         T5.Data2(B5, alpha, MK5);
         Put_Line("T10 : Send MK, B, alpha to T5.");

         countA(m, B1, alpha, C10, MO10, MK1, A);

         accept Result (A1 : out Vector1H) do
            A1 := A;
         end Result;

         Put_Line("Task10 finished!");
      end T10;

   begin
      null;
   end tasks;
   k : Integer := 100;
   t:time;
   s1,s3:Day_Duration;
   s2,s4,s900,s1200,s1500:Float;
begin
   Put_Line("Main started!");
   for i in 1..3 loop
      k := k + 100;
      t:=clock;
      s1:=Seconds(t);
      s2:=Float(s1);
      for j in 1..k loop
         tasks;
      end loop;
      t:=clock;
      s3:=Seconds(t);
      s4:=Float(s3);
      if i=1 then
         s900:=s4-s2;
      elsif i=2 then
         s1200:=s4-s2;
      elsif i=3 then
         s1500:=s4-s2;
      end if;
   end loop;
   put(s900,10,20,0);
   New_Line;
   put(s1200,10,20,0);
   New_Line;
   put(s1500,10,20,0);
   New_Line;
   Put_Line("Main finished");
end prg_lp;
