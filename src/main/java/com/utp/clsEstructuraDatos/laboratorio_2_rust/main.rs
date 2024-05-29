#![allow(non_snake_case)]
#![allow(unused_imports)]
#![allow(clippy::needless_return)]

use std::error::Error;
use std::ops::Mul;
use std::thread::spawn;

use inquire::formatter::OptionFormatter;
use inquire::list_option::ListOption;
use inquire::{Confirm, InquireError, MultiSelect, Select, Text};
fn main() {
    // let fibonaccies: [u128; 20] = [
    //     0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181,
    // ];
    for i in 1..=45 {
        let num = fib(i).expect("No es fibonacci de 0");
        println!("Fib({i}) = {num}");
        // assert_eq!(fibonaccies[i as usize - 1], num)
    }

    for i in 1..20 {
        let num = factorial(i);
        println!("factorial({i}) = {}", num.unwrap());
    }

    let _ = dbg!(Q(2, 3));
    let _ = dbg!(Q(14, 3));
    let _ = dbg!(Q(5861, 7));
    spawn_app();
}

fn spawn_app() {
    use inquire::InquireError::*;
    let funciones = vec!["Fibonacci", "Factorial", "Q", "L"];
    'app: loop {
        let ans = loop {
            break match Select::new("Elija una función", funciones.clone())
            .with_help_message("↑↓ para mover, {Enter} para seleccionar, escriba para filtrar")
            .prompt() {
                Ok(ans) => ans,
                Err(error) => {
                    println!("Error: {error}");
                    if matches!(error, OperationCanceled | OperationInterrupted) {
                        break 'app;
                    }
                    continue;
                }
            };
        };

        let funcion = Funcs::from_str(ans).unwrap(); // El selector solo tiene funciones válidad cargadas
        let args = loop {
            break match leer_argumentos(funcion) {
                Ok(args) => args,
                Err(error) => {
                    println!("Error: {error:?}");
                    if matches!(error, OperationCanceled | OperationInterrupted) {
                        break 'app;
                    }
                    continue;
                }
            };
        };
        #[rustfmt::skip]
        match funcion {
            Funcs::Q => {println!("Q({}, {}) = {}", args[0], args[1], Q(args[0], args[1]))},
            Funcs::L => {println!("L({}) = {}", args[0], L(args[0]))},
            Funcs::Fibonacci => {
                let Some(fibonacci) = fib(args[0]) else {
                    println!("Fibonacci({}) no es calculable", args[0]);
                    continue;
                };
                println!("Fibonacci({}) = {fibonacci}", args[0])
            },
            Funcs::Factorial => {
                let Some(factorial) = factorial(args[0]) else {
                    println!("Factorial({}) no es calculable", args[0]);
                    continue;
                };
                println!("Factorial({}) = {factorial}", args[0])
            },            
        };
        match Confirm::new("Desea salir?").with_placeholder("Y/N").prompt() {
            Ok(_) | Err(OperationInterrupted)  => break,
            _=>(),
        }
    }
    println!("Adios!");
}

fn leer_argumentos(funcion: Funcs) -> Result<Vec<usize>, InquireError> {
    let help_message = match funcion {
        Funcs::Q => "Ejemplo: 25, 2",
        _ => "Ejemplo: 24"
    };
    let ans = Text::new(&format!("Ingrese los argumentos de {funcion}."))
        .with_help_message(help_message)
        .prompt()?
        .split(',')
        .map(|arg| arg.parse::<usize>())
        .collect::<Result<Vec<usize>, _>>()
        .map_err(|e| InquireError::Custom(format!("Error: {e}").into()))?;
    use Funcs::*;
    let argumentos_requeridos = match funcion {
        Fibonacci => 1,
        Factorial => 1,
        Q => 2,
        L => 1,
    };
    (ans.len() >= argumentos_requeridos).then_some(()).ok_or(InquireError::Custom(
        format!(
            "La función {funcion:?} requiere `{argumentos_requeridos}` argumento(s), pero se \
             escribieron `{}` argumentos",
            ans.len()
        )
        .into(),
    ))?;
    Ok(ans)
}

#[derive(Debug, Copy, Clone)]
enum Funcs {
    Fibonacci,
    Factorial,
    Q,
    L,
}
impl Funcs {
    fn from_str(s: &str) -> Option<Self> {
        match s {
            "Fibonacci" => Some(Self::Fibonacci),
            "Factorial" => Some(Self::Factorial),
            "Q" => Some(Self::Q),
            "L" => Some(Self::L),
            _ => None,
        }
    }
}
impl std::fmt::Display for Funcs {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        match self {
            Funcs::Fibonacci => write!(f, "Fibonacci(n)"),
            Funcs::Factorial => write!(f, "Factorial(n)"),
            Funcs::Q => write!(f, "Q(n,b)"),
            Funcs::L => write!(f, "L(n)"),
        }
    }
}

fn fib(nth: usize) -> Option<u128> {
    let mut left: u128 = 0;
    let mut right: u128 = 1;
    let mut result: u128 = 0;
    match nth {
        0 => return None, // Fibonacci de 0 no es calculable
        1 => return Some(0),
        2 => return Some(1),
        _ => (),
    }

    for _ in 3..=nth {
        result = left.checked_add(right)?;
        left = right;
        right = result;
    }
    return Some(result);
}

/// Factorial Recursivo
fn factorial(n: usize) -> Option<u128> {
    if let 0..=1 = n {
        return Some(1);
    } else {
        return (n as u128).checked_mul(factorial(n - 1)?);
    }
}

fn Q(a: usize, b: usize) -> usize {
    if a < b {
        return 0;
    }
    return Q(a - b, b) + 1;
}

fn L(n: usize) -> usize {
    if n == 1 {
        return 0;
    }
    return L(n / 2) + 1;
}
